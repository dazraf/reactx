package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.classType
import io.dazraf.reactx.example.simple.text
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.hasClass
import kotlin.dom.removeClass

fun MdlComponent.text(cssClassId: String = "", init: Text.() -> Unit): Element {
  return mainElement.text(cssClassId, init)
}

fun Element.text(cssClassId: String = "", init: Text.() -> Unit): Element {
  val t = Text(cssClassId = cssClassId)
  t.init()
  this.append(t.mainElement)
  return t.mainElement
}

class Text(cssClassId: String = "") : MdlComponent("div", "mdl-textfield mdl-js-textfield", cssClassId) {
  private val input = document.createElement("input") as HTMLInputElement
  private val label = document.createElement("label") as HTMLLabelElement

  init {
    label.htmlFor = id
    label.textContent = labelText
    label classType "mdl-textfield__label"
    input.id = id
    input.type = "text"
    input classType "mdl-textfield__input"
    mainElement.append(input, label)
  }

  override var id: String
    get() = input.id
    set(value) {
      input.id = id
      label.htmlFor = id
    }

  var labelText: String
    get() = label.textContent ?: ""
    set(value) {
      label.textContent = value
    }

  var isFloating : Boolean
  get() = mainElement.hasClass("mdl-textfield--floating-label")
  set(value) {
    if (isFloating != value) {
      if (value) {
        mainElement.addClass("mdl-textfield--floating-label")
      } else {
        mainElement.removeClass("mdl-textfield--floating-label")
      }
    }
  }
}