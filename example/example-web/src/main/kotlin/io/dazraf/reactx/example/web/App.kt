package io.dazraf.reactx.example.web

import io.dazraf.reactx.Invoker
import io.dazraf.reactx.example.reakt.vdom.node.*
import io.dazraf.reactx.example.reakt.vdom.log
import io.dazraf.reactx.example.reakt.vdom.render.Reakt
import io.dazraf.reactx.parse
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document

data class Name(val first: String, val last: String)

/**
 * Example main function. Started at script body.
 * At first time you have to run `mvn package`.
 * Open example index.html in browser once you compile it.
 */
fun main(args: Array<String>) {
  Invoker().get("/api/test").parse<Name>().then({ console.log("result:", it)}, { console.log("failed: ${it.message}")})
  App().run()
}

class App {
  private val items = mutableListOf(1)
  private var last = 1

  fun run() {
    log.debug = true
    val container = document.getElementById("app")
    if (container == null) {
      log.error("can't find container 'app'")
    } else {
      render()
//      render(container)
    }
  }

  private fun render() {
    val container = document.getElementById("app")!!
    val root = root {
      ul {
        id = "list"
        li {
          id = "one"
          + "one"
        }
        li {
          id = "two"
          + "two"
        }
      }
    }
    container.appendChild(root.render())
    val ul = document.getElementById("list")!!
    val one = document.getElementById("one")!!
    ul.appendChild(one)
  }

  private fun render(container: Element) {
    val root = createVDom(container)
    Reakt.render(root, container)
  }

  private fun createVDom(container: Element): VElement<HTMLDivElement> {
    val root = root {
      button {
        elementClass = "btn btn-primary"
        onClick = {
          items += ++last
          log.debug("size: ${items.size}")
          render(container)
        }
        +"Push"
      }
      button {
        elementClass = "btn btn-success"
        onClick = {
          items.removeAt(0)
          log.debug("size: ${items.size}")
          render(container)
        }
        disabled = items.size == 0
        +"Pop"
      }
      ul {
        elementClass = "list-group"
        items.forEach { item ->
          li {
            key = "$item"
            elementClass = "list-group-item"
            img {
              src = "https://randomuser.me/api/portraits/men/$item.jpg"
            }
            + " Item $item"
          }
        }
      }
    }
    return root
  }
}
