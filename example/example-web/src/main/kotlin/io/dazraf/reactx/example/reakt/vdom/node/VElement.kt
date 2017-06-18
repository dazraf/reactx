package io.dazraf.reactx.example.reakt.vdom.node

import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.reflect.KProperty

open class VElement<H: Element>(open var id: String = "",
                                   open var key: String = "",
                                   open val tag: String = "",
                                   var elementClass: String = "") : VNode<H>()
{
  override val nodeType: Short
    get() = 1 // "Element"

  @Suppress("UNCHECKED_CAST")
  override fun createNode(): H {
    return document.createElement(tag) as H
  }

  private val state = mutableMapOf<String, Any>()

  @Suppress("UNCHECKED_CAST")
  protected class State<T : Any, H : Element> (val default: T){
    operator fun getValue(thisRef: VElement<H>, property: KProperty<*>): T {
      return (thisRef.state[property.name] as T?) ?: default
    }

    operator fun setValue(thisRef: VElement<H>, property: KProperty<*>, value: T) {
      thisRef.state[property.name] = value
    }
  }

  var text : String by State("")


  operator fun String.unaryPlus() {
    children += VText(this)
  }

  @Suppress("UNCHECKED_CAST")
  override fun render() : H {
    val element = super.render()
    pushProps(element)
    return element
  }

  open fun pushProps(element: H) {
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
}

fun root(fn: VElement<HTMLDivElement>.() -> Unit): VElement<HTMLDivElement> {
  val root = VDiv()
  root.fn()
  return root
}



