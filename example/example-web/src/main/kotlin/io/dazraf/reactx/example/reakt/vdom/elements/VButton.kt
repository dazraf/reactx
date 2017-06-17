package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import kotlin.browser.document

class VButton : VElement(tag = "button") {
  var onClick : VButton.(event: VMouseEvent) -> Unit = {}
  var type = "button"
  var disabled = false

  override fun pushProps(element: Element) {
    super.pushProps(element)
    if (element is HTMLButtonElement) {
      bindOnClick(element)
      element.type = type
      element.disabled = disabled
    }
  }
  override fun render() : Element {
    val element = document.createElement(tag)
    val b = element as HTMLButtonElement
    bindOnClick(b)
    pushProps(element)
    return element
  }

  private fun bindOnClick(button: HTMLButtonElement) {
    button.onclick = { event ->
      onClick(VMouseEvent(this, event))
    }
  }
}

fun VElement.button(fn: VButton.()->Unit) : VButton {
  val button = VButton()
  button.fn()
  this.children.add(button)
  return button
}