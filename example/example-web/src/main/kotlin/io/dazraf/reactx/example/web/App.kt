package io.dazraf.reactx.example.web

import io.dazraf.reactx.example.reakt.vdom.div
import io.dazraf.reactx.example.reakt.vdom.root

/**
 * Example main function. Started at script body.
 * At first time you have to run `mvn package`.
 * Open example index.html in browser once you compile it.
 */
fun main(args: Array<String>) {
  App().run()
}

class App {
  fun run() {
    val state1 = root {
      div {
        elementClass = "my-div"
        + "hello"
      }
    }

    val state2 = root {
      div {
        elementClass = "my-div"
        + "hello, world"
      }
    }
  }
}





