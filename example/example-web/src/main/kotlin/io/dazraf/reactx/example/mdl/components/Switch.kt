package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.plus
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

fun MdlComponent.switch(cssClassId: String = "", init: Switch.() -> Unit): Element {
  return mainElement.switch(cssClassId, init)
}

fun Element.switch(cssClassId: String = "", init: Switch.() -> Unit): Element {
  val toggle = Switch(cssClassId)
  toggle.init()
  this + toggle.mainElement
  return toggle.mainElement
}

class Switch(cssClassId: String = "") : MdlComponent("label", "mdl-switch mdl-js-switch mdl-js-ripple-effect", cssClassId) {
  private var _id: String = "toggle"
  private val input = (document.createElement("input") as HTMLInputElement).apply {
    addClass("mdl-switch__input")
    type = "checkbox"
  }

  private val label = document.createElement("span").apply {
    addClass("mdl-switch__label")
  }

  override var id: String
    get() {
      return _id
    }
    set(value) {
      _id = value
      input.id = value
      (mainElement as HTMLLabelElement).htmlFor = value
    }
  var textLabel by htmlTextPram(parent = label)
  var hasRipple by classFlag("mdl-js-ripple-effect")
  var checked: Boolean
    get() = input.checked
    set(value) {
      input.checked = value
    }

  init {
    mainElement + label
    mainElement + input
    id = _id
    checked = false
  }

  fun onchange(callback: ((Event, Switch) -> Unit)?) {
    if (callback == null) {
      input.onchange = null
    } else {
      input.onchange = {
        callback(it, this)
      }
    }
  }
}