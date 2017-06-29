package io.dazraf.reactx.example.reakt.vdom.node

import org.w3c.dom.HTMLButtonElement

class VButton : VElement<HTMLButtonElement>(tag = "button") {
  var onClick: VButton.(event: VMouseEvent<VButton>) -> Unit = {}
  var type = "button"
  var disabled = false

  override fun pushProps(element: HTMLButtonElement) {
    super.pushProps(element)
    bindOnClick(element)
    element.type = type
    element.disabled = disabled
  }

  private fun bindOnClick(button: HTMLButtonElement) {
    button.onclick = { event ->
      onClick(VMouseEvent(this, event))
    }
  }
}

fun VElement<*>.button(fn: VButton.() -> Unit): VButton {
  val button = VButton()
  button.fn()
  appendChild(button)
  return button
}