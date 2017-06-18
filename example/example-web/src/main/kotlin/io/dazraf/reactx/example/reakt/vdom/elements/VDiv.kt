package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.HTMLDivElement

class VDiv : VElement<HTMLDivElement>(tag = "div")

fun VElement<*>.div(fn: VDiv.() -> Unit) : VDiv {
  val div = VDiv()
  div.fn()
  this.children.add(div)
  return div
}
