package io.dazraf.reactx.example.reakt.vdom.elements

class VDiv : VElement(tag = "div")

fun VElement.div(fn: VDiv.() -> Unit) : VDiv {
  val div = VDiv()
  div.fn()
  this.children.add(div)
  div.addListener(this::makeDirty)
  return div
}
