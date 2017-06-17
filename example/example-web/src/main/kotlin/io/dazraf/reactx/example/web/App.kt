package io.dazraf.reactx.example.web

import io.dazraf.reactx.example.reakt.vdom.elements.*
import io.dazraf.reactx.example.reakt.vdom.render.Reakt
import org.w3c.dom.Element
import kotlin.browser.document

/**
 * Example main function. Started at script body.
 * At first time you have to run `mvn package`.
 * Open example index.html in browser once you compile it.
 */
fun main(args: Array<String>) {
  App().run()
}

class App {
  private val items = mutableListOf(1)
  private var last = 1

  fun run() {
    val container = document.getElementById("app")
    if (container == null) {
      console.log("can't find container 'app'")
    } else {
      render(container)
    }
  }

  private fun createVDom(container: Element): VElement {
    val root = root {
      button {
        elementClass = "btn btn-primary"
        onClick = {
          items += ++last
          render(container)
        }
        + "Push"
      }
      button {
        elementClass = "btn btn-success"
        onClick = {
          items.removeAt(0)
          console.log("remaining: ${items.size}")
          render(container)
        }
        disabled = items.size == 0
        + "Pop"
      }
      ul {
        elementClass = "list-group"
        items.forEach { item ->
          li {
            key = "$item"
            elementClass = "list-group-item"
            +"Item $item"
          }
        }
      }
    }
    return root
  }

  private fun render(container: Element) {
    val root = createVDom(container)
    Reakt.render(root, container)
  }
}





