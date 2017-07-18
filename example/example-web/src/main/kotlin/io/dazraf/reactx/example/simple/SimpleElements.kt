package io.dazraf.reactx.example.simple

import org.w3c.dom.*
import kotlin.browser.document

fun prepareContainer(containerId: String, fn: Element.() -> Unit) {
  val container = document.getElementById(containerId) ?: {
    val c = document.createElement("div")
    c.id = containerId
    document.body?.insertBefore(c, null)
    c
  }()
  container.fn()
}

fun Element.div(fn: HTMLDivElement.() -> Unit): HTMLDivElement {
  return create("div", fn)
}

fun Element.span(fn: HTMLSpanElement.() -> Unit): HTMLSpanElement {
  return create("span", fn)
}

fun Element.nav(fn: HTMLElement.() -> Unit): HTMLElement {
  return create("nav", fn)
}

fun Element.a(fn: HTMLAnchorElement.() -> Unit): HTMLAnchorElement {
  return create("a", fn)
}

infix fun Element.text(text: String): Text {
  val node = document.createTextNode(text)
  insertBefore(node, null)
  return node
}


fun Element.ul(fn: HTMLUListElement.() -> Unit): HTMLUListElement {
  return create("ul", fn)
}

fun HTMLUListElement.li(fn: HTMLLIElement.() -> Unit): HTMLLIElement {
  return create("li", fn)
}

fun Element.i(fn: HTMLElement.() -> Unit): HTMLElement {
  return create("i", fn)
}

fun Element.button(fn: HTMLButtonElement.() -> Unit): HTMLButtonElement {
  return create("button", fn)
}

@Suppress("UNCHECKED_CAST")
fun <T : Element> Element.create(tag: String, fn: T.() -> Unit): T {
  val e = document.createElement(tag) as T
  e.fn()
  insertBefore(e, null)
  return e
}

operator fun Element.plusAssign(text: String) {
  this.text(text)
}

operator fun Element.plus(text: String) {
  this.text(text)
}


