package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.plus
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

fun MdlComponent.checkbox(cssClassId: String = "", init: CheckBox.() -> Unit): Element {
  return mainElement.checkbox(cssClassId, init)
}

fun Element.checkbox(cssClassId: String = "", init: CheckBox.() -> Unit): Element {
  val toggle = CheckBox(cssClassId)
  toggle.init()
  this + toggle.mainElement
  return toggle.mainElement
}

class CheckBox(cssClassId: String = "") : MdlComponent("label", "mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect", cssClassId) {
  private var _id: String = "toggle"
  private val input = (document.createElement("input") as HTMLInputElement).apply {
    addClass("mdl-checkbox__input")
    type = "checkbox"
  }

  private val label = document.createElement("span").apply {
    addClass("mdl-checkbox__label")
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
    mainElement + input
    mainElement + label
    id = _id
    checked = false
  }

  fun onchange(callback: ((Event, CheckBox) -> Unit)?) {
    if (callback == null) {
      input.onchange = null
    } else {
      input.onchange = {
        callback(it, this)
      }
    }
  }
}