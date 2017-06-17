package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.reflect.KProperty

open class VElement(open var id: String = "",
                    open var key: String = "",
                    open val tag: String = "",
                    var elementClass: String = "",
                    val children: MutableList<VElement> = mutableListOf<VElement>())
{
  private val state = mutableMapOf<String, Any>()
  private val listeners = mutableListOf<() -> Unit>()

  @Suppress("UNCHECKED_CAST")
  protected class State<T : Any> (val default: T){
    operator fun getValue(thisRef: VElement, property: KProperty<*>): T {
      return (thisRef.state[property.name] as T?) ?: default
    }

    operator fun setValue(thisRef: VElement, property: KProperty<*>, value: T) {
      thisRef.state[property.name] = value
      thisRef.makeDirty()
    }
  }

  var text : String by State("")

  fun addListener(listener: () -> Unit) {
    listeners += listener
  }

  open fun makeDirty() {
    listeners.forEach { it() }
  }

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
    if (text != "") {
      element.textContent = text
    }
  }

  open fun shouldNodeUpdate() = true
}

fun root(fn: VElement.() -> Unit): VElement {
  val root = VElement()
  root.fn()
  return root
}



