@file:Suppress("NOTHING_TO_INLINE")

package io.dazraf.reactx

object log {
  var debug = false
  var info = true

  inline fun error(vararg items: Any?) {
    console.log(arrayOf("ERROR:", *items))
  }
  inline fun warn(vararg items: Any?) {
    console.log(arrayOf("WARN:", *items))
  }

  inline fun info(vararg items: Any?) {
    if (info) {
      console.log(arrayOf("INFO:", *items))
    }
  }
  inline fun debug(vararg items: Any?) {
    if (debug) {
      console.log(arrayOf("DEBUG:", *items))
    }
  }

  inline fun table(value: List<Any>) {
    js("console").table(value)
  }
}
