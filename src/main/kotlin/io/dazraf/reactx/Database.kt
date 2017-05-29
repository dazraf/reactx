package io.dazraf.reactx

import org.bson.conversions.Bson
import rx.Observable
import rx.Single
import kotlin.reflect.KClass

enum class OpType {
  INSERT,
  UPDATE,
  DELETE
}
data class Operation<out T : Any>(val type: OpType, val value: T)

interface Database {
  fun close()
  fun <T : Any> getCollection(name: String, klass: KClass<T>): DatabaseCollection<T>
}

inline fun <reified T: Any> Database.getCollection(name: String) = this.getCollection(name, T::class)

interface DatabaseCollection<T : Any>{
  fun insert(value: T) : Single<T>
  fun find(filter: Bson) : Observable<T>
  fun events() : Observable<Operation<T>>
}

