package io.dazraf.reactx.db.mongo

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoCollection
import io.dazraf.reactx.Database
import io.dazraf.reactx.DatabaseCollection
import io.dazraf.reactx.OpType
import io.dazraf.reactx.Operation
import io.vertx.core.Vertx
import io.vertx.rx.java.ContextScheduler
import org.bson.conversions.Bson
import rx.Observable
import rx.Single
import rx.subjects.PublishSubject
import kotlin.reflect.KClass

class DatabaseMongo(private val client: MongoClient, val name: String, val vertx: Vertx) : Database {
  override fun <T : Any> getCollection(name: String, klass: KClass<T>): DatabaseCollection<T> {
    return DatabaseCollectionMongo(client.getDatabase(name).getCollection(name, klass.java), vertx)
  }

  override fun close() {
    client.close()
  }
}

class DatabaseCollectionMongo<T : Any>(private val collection: MongoCollection<T>, val vertx: Vertx) : DatabaseCollection<T> {
  override fun find(filter: Bson): Observable<T> {
    return collection.find(filter).toObservable()
  }

  private val subject = PublishSubject.create<Operation<T>>()

  override fun insert(value: T): Single<T> {
    return collection.insertOne(value)
      .toSingle()
      .map { value }
      .doOnSuccess { subject.onNext(Operation(OpType.INSERT, it)) }
  }

  override fun events(): Observable<Operation<T>> {
    return subject.observeOn(ContextScheduler(vertx, false, true))
  }
}
