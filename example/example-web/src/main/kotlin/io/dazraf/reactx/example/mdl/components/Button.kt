package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.plus
import org.w3c.dom.Element
import org.w3c.dom.events.Event

inline fun MdlComponent.button(cssClassId: String = "",
                               isFab: Boolean = false,
                               isJsButton: Boolean = true,
                               isRaised: Boolean = true,
                               hasRipple: Boolean = true,
                               isColored: Boolean = true,
                               isAccented: Boolean = false,
                               init: Button.() -> Unit): Element {
  return this.mainElement.button(
    cssClassId = cssClassId,
    isFab = isFab,
    isJsButton = isJsButton,
    isRaised = isRaised,
    hasRipple = hasRipple,
    isColored = isColored,
    isAccented = isAccented,
    init = init)
}

inline fun Element.button(cssClassId: String = "",
                          isFab: Boolean = false,
                          isJsButton: Boolean = true,
                          isRaised: Boolean = true,
                          hasRipple: Boolean = true,
                          isColored: Boolean = true,
                          isAccented: Boolean = false,
                          init: Button.() -> Unit): Element {
  val button = Button(
    cssClassId = cssClassId,
    isFab = isFab,
    isJsButton = isJsButton,
    isRaised = isRaised,
    isColored = isColored,
    isAccented = isAccented,
    hasRipple = hasRipple)
  button.init()
  this + button.mainElement
  return button.mainElement
}

class Button(val cssClassId: String = "",
             val isFab: Boolean,
             val isJsButton: Boolean,
             val isRaised: Boolean,
             val hasRipple: Boolean,
             val isColored: Boolean,
             val isAccented: Boolean) :
  MdlComponent("button",
    " mdl-button" +
      (if (isFab)     " mdl-button--fab" else "") +
      (if (isJsButton) " mdl-js-button" else "") +
      (if (isRaised)  " mdl-button--raised" else "") +
      (if (hasRipple) " mdl-js-ripple-effect" else "") +
      (if (isColored) " mdl-button--colored" else "") +
      (if (isAccented) " mdl-button--accent" else "")
    , cssClassId) {

  var onclick : ((Event) -> dynamic)?
  get() = (mainElement as org.w3c.dom.HTMLButtonElement).onclick
  set(value) { (mainElement as org.w3c.dom.HTMLButtonElement).onclick = value }


  var ondblclick: ((Event) -> dynamic)?
    get() = (mainElement as org.w3c.dom.HTMLButtonElement).ondblclick
    set(value) { (mainElement as org.w3c.dom.HTMLButtonElement).ondblclick = value }
}
