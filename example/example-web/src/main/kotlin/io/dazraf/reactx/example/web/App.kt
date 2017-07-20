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
      title = "New User"
      mainElement.form {
        action = "#"
        text {
          id = "name"
          labelText = "Name"
          isFloating = true
        }
        text {
          id = "username"
          labelText = "Username"
          isFloating = true
        }
        text {
          id = "password"
          labelText = "Password"
          isFloating = true
          isPassword = true
        }
        checkbox {
          id = "is-userid"
          textLabel = "Super user"
        }
        checkbox {
          id = "can-read"
          textLabel = "Can Read"
        }
        checkbox {
          id = "can-write"
          textLabel = "Can Write"
        }
        switch {
          id = "activated"
          textLabel = "Activated"
          checked = true
        }
      }
    }
  }
}
