package io.dazraf.reactx.example.reakt.vdom

import org.w3c.dom.Element
import kotlin.browser.document

open class VElement(open var id: String = "",
                    open var key: String = "",
                    open val tag: String = "div",
                    var elementClass: String = "",
                    var text: String = "",
                    val children: MutableList<VElement> = mutableListOf<VElement>(),
                    val properties : MutableMap<String, String> = mutableMapOf<String, String>())
{
  operator fun String.unaryPlus() {
    text += this
  }

  open fun render() : Element {
    val element = document.createElement(tag)
    pushProps(element)
    appendChildren(element)
    return element
  }

  private fun appendChildren(element: Element) {
    children.forEach {
      element.appendChild(it.render())
    }
  }

  open fun pushProps(element: Element) {
    if (elementClass != "") {
      element.className = elementClass
    }
    if (id != "") {
      element.id = id
    }
    if (key != "") {
      element.setAttribute("key", key)
    }
  }

  open fun shouldNodeUpdate() = true
}

class VDiv : VElement(tag = "div")

fun root(fn: VElement.() -> Unit): VElement {
  val root = VElement()
  root.fn()
  return root
}

fun VElement.div(fn: VDiv.() -> Unit) : VDiv {
  val div = VDiv()
  div.fn()
  this.children.add(div)
  return div
}


