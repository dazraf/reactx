package io.dazraf.reactx.example.web

import io.dazraf.reactx.example.web.NotificationType.*

data class ToDo(val summary: String, val detail: String = "", val id: String? = null)
enum class NotificationType {
  HELLO,
  ADD,
  REMOVE,
  CLEAR
}

data class Notification(val type: NotificationType, val value: ToDo? = null)
interface ToDoStore {
  fun list(): List<ToDo>
  fun add(todo: ToDo): ToDo
  fun remove(id: String)
  fun clear()
  fun onChange(fn: (Notification)-> Unit)
}

class ToDoStoreImpl : ToDoStore {
  private val todos = mutableMapOf<String, ToDo>()
  private val ids = generateSequence(1, { it + 1 }).iterator()
  private val observers = mutableListOf<(Notification) -> Unit>()

  override fun list(): List<ToDo> {
    return todos.values.toList()
  }

  override fun add(todo: ToDo): ToDo {
    val data = todo.copy(id = ids.next().toString())
    todos[data.id!!] = data
    notify(Notification(ADD, data))
    return data
  }

  override fun remove(id: String) {
    if (todos.containsKey(id)) {
      notify(Notification(REMOVE, todos[id]))
      todos.remove(id)
    }
  }

  override fun clear() {
    todos.clear()
    notify(Notification(CLEAR))
  }

  override fun onChange(fn: (Notification) -> Unit) {
    observers += fn
    fn(Notification(HELLO))
  }

  private fun notify(notification: Notification) {
    observers.forEach { it(notification) }
  }
}