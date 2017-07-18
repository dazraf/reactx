package io.dazraf.reactx.example.web

import io.dazraf.reactx.example.mdl.components.*
import io.dazraf.reactx.example.simple.text
import io.dazraf.reactx.log
import kotlin.browser.document

/**
 * Example main function. Started at script body.
 * At first time you have to run `mvn package`.
 * Open example index.html in browser once you compile it.
 */
fun main(args: Array<String>) {
  log.debug = true
  App().run()
}

class App {
  fun run() {
    log.debug = true
    val root = document.getElementById("app")
    if (root == null) {
      log.error("couldn't find 'root' element")
      return
    }
    root.card {
      title = "Welcome"
      supportingText = """
      Lorem ipsum dolor sit amet, consectetur adipiscing elit.
      Mauris sagittis pellentesque lacus eleifend lacinia...
      """
      actions {
        button(isFab = true) {
          id = "btn-id"
          icon("content_paste")
        }
      }
    }
    root.text {
      id = "text-1"
      labelText = "My Label"
      isFloating = true
    }
  }
}
