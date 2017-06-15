package io.dazraf.reactx.db.mongo

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoCollection
import io.dazraf.reactx.*
import io.vertx.core.Vertx
import io.vertx.rx.java.ContextScheduler
import org.bson.*
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.MongoId
import org.litote.kmongo.util.KMongoUtil
import rx.Observable
import rx.Single
import rx.subjects.PublishSubject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class DatabaseMongo(private val client: MongoClient, val name: String, val vertx: Vertx) : Database {
  override fun <T : Any> getCollection(name: String, klass: KClass<T>): DatabaseCollection<T> {
    return DatabaseCollectionMongo(client.getDatabase(name).getCollection(name, klass.java), vertx)
  }

  override fun close() {
    client.close()
  }
}

class DatabaseCollectionMongo<T : Any>(private val collection: MongoCollection<T>, val vertx: Vertx) : DatabaseCollection<T> {
  override fun updateOne(filter: Bson, value: T): Single<T> {
    return collection.findOneAndUpdate(filter, KMongoUtil.toBson(KMongoUtil.setModifier(value)))
      .first()
      .toSingle()
      .onErrorResumeNext { Single.error(RuntimeException("could not update $filter", it)) }
      .doOnSuccess {
        assingId(it, value)
        subject.onNext(UpdateOp(it, value))
      }
  }

  override fun findOne(filter: Bson): Single<T> {
    return collection.find(filter)
      .first().toSingle()
      .onErrorResumeNext { Single.error(RuntimeException("failed to find $filter")) }
  }

  override fun find(filter: Bson, skip: Int, count: Int): Observable<T> {
    return collection.find(filter).skip(skip).toObservable().take(count)
  }

  private val subject = PublishSubject.create<Op<T>>()

  override fun insert(value: T): Single<T> {
    return collection.insertOne(value)
      .toSingle()
      .map { value }
      .doOnSuccess { subject.onNext(InsertOp(value)) }
  }

  override fun events(): Observable<Op<T>> {
    return subject.observeOn(ContextScheduler(vertx, false, true))
  }

  private fun <T : Any> assingId(from: T, to: T) {
    val id = KMongoUtil.extractId(from, from.javaClass.kotlin)
    val idProperty = MongoIdUtil.findIdProperty(to.javaClass.kotlin)
    if (idProperty != null) {
      MongoIdUtil.setIdValue(idProperty, to, id)
    }
  }
}


object MongoIdUtil {

  //TODO need to cache something here
  fun findIdProperty(type: KClass<*>): KMutableProperty1<*, *>?
    = getAnnotatedMongoIdProperty(type) ?: getIdProperty(type)

  private fun getIdProperty(type: KClass<*>): KMutableProperty1<*, *>?
    =
    try {
      type.memberProperties.filter { "_id" == it.name }.map { it as KMutableProperty1<*, *> }.first()
    } catch(error: Throwable) {
      //ignore
      null
    }

  fun getAnnotatedMongoIdProperty(type: KClass<*>): KMutableProperty1<*, *>?
    =
    try {
      type.memberProperties.filter { it.annotations.any { it is MongoId } }.map { it as KMutableProperty1<*, *> }.first()
    } catch(error: Throwable) {
      //ignore
      null
    }

  fun getIdValue(idProperty: KMutableProperty1<*, *>, instance: Any): Any? {
    idProperty.isAccessible = true
    return (idProperty)(instance)
  }

  fun setIdValue(idProperty: KMutableProperty1<*, *>, instance: Any, value: Any) {
    idProperty.isAccessible = true
    (idProperty.setter)(instance, value)
  }

  fun getIdBsonValue(idProperty: KMutableProperty1<*, *>, instance: Any): BsonValue? {
    val idValue = (idProperty)(instance)
    return when (idValue) {
      null -> null
      is ObjectId -> BsonObjectId(idValue)
      is String -> BsonString(idValue)
      is Double -> BsonDouble(idValue)
      is Int -> BsonInt32(idValue)
      is Long -> BsonInt64(idValue)
    //TODO direct mapping
      else -> KMongoUtil.toBson(KMongoUtil.toExtendedJson(idValue))
    }
  }
}