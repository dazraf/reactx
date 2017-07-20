package io.dazraf.reactx.example.web

import io.dazraf.reactx.example.form
import io.dazraf.reactx.example.mdl.components.*
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
        button {
          id = "btn-id"
          isFab = true
          icon("content_paste")
        }
      }
    }
    root.form {
      action = "#"
      text {
        id = "text-1"
        labelText = "My Label"
        isFloating = true
      }
    }
    root.form {
      action = "#"
      text {
        id  = "text-2"
        pattern = "-?[0-9]*(\\.[0-9]+)?"
        errorString = "value must be a number"
        expandWith {
          icon("search")
        }
      }
      checkbox {
        id = "checkbox-1"
        textLabel = "checkbox 1"
        onchange { event, toggle ->
          log.info(event, toggle.checked)
        }
      }
      checkbox {
        id = "checkbox-2"
        checked = true
      }
      switch("switch") {
        id = "switch-1"
        onchange { event, switch ->
          log.info(event, switch.checked)
        }
      }
      switch("switch") {
        id = "switch-2"
        checked = true
      }
    }
  }
}
