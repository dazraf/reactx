package io.dazraf.reactx.example.mdl.components


import org.w3c.dom.Element
import org.w3c.dom.events.Event

inline fun MdlComponent.button(cssClassId: String = "",
                               init: Button.() -> Unit): Element {
  return this.mainElement.button(cssClassId = cssClassId, init = init)
}

inline fun Element.button(cssClassId: String = "", init: Button.() -> Unit): Element {
  val button = Button(cssClassId = cssClassId)
  button.init()
  this.append(button.mainElement)
  return button.mainElement
}

class Button(cssClassId: String = "") :
  MdlComponent("button", " mdl-button", cssClassId) {

  var isFab by classFlag("mdl-button--fab")
  var isJsButton by classFlag("mdl-js-button")
  var isRaised by classFlag("mdl-button--raised")
  var hasRipple by classFlag("mdl-js-ripple-effect")
  var isColored by classFlag(" mdl-button--colored")
  var isAccented by classFlag("mdl-button--accent")
  var onclick: ((Event) -> dynamic)?
    get() = (mainElement as org.w3c.dom.HTMLButtonElement).onclick
    set(value) {
      (mainElement as org.w3c.dom.HTMLButtonElement).onclick = value
    }
  var ondblclick: ((Event) -> dynamic)?
    get() = (mainElement as org.w3c.dom.HTMLButtonElement).ondblclick
    set(value) {
      (mainElement as org.w3c.dom.HTMLButtonElement).ondblclick = value
    }
  init {
    isJsButton = true
    isRaised = true
    hasRipple = true
    isColored = true
  }
}
