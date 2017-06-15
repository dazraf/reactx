package io.dazraf.reactx

import org.bson.conversions.Bson
import rx.Observable
import rx.Single
import kotlin.reflect.KClass

abstract class Op<out T : Any>(open val value: T)
data class InsertOp<out T : Any>(override val value: T) : Op<T>(value)

data class UpdateOp<out T : Any>(val before: T, override val value: T) : Op<T>(value)

data class deleteOp<out T : Any>(override val value: T) : Op<T>(value)

interface Database {
  fun close()
  fun <T : Any> getCollection(name: String, klass: KClass<T>): DatabaseCollection<T>
}

inline fun <reified T : Any> Database.getCollection(name: String) = this.getCollection(name, T::class)

interface DatabaseCollection<T : Any> {
  fun events(): Observable<Op<T>>

  fun insert(value: T): Single<T>
  fun find(filter: Bson, skip: Int, count: Int): Observable<T>
  fun findOne(filter: Bson): Single<T>
  fun updateOne(filter: Bson, value: T): Single<T> // value must have an id
}

