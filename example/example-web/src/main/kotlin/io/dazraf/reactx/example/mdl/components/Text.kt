package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.classType
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import kotlin.browser.document
import kotlin.dom.addClass

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
  private var _id: String = ""

  private val error by lazy {
    document.createElement("span").apply {
      addClass("mdl-textfield__error")
      mainElement.append(this)
    }
  }

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
    get() = _id
    set(value) {
      _id = value
      input.id = value
      label.htmlFor = value
    }

  var labelText: String
    get() = label.textContent ?: ""
    set(value) {
      label.textContent = value
    }

  var isFloating by classFlag("mdl-textfield--floating-label")
  var pattern: String by htmlPram(input)
  var errorString: String by htmlTextPram(parent = error)
  var isPassword : Boolean
  get() = input.type == "password"
  set(value) { input.type = if (value) "password" else "text"}

  fun expandWith(initContents: HTMLLabelElement.() -> Unit) : HTMLLabelElement {
    isFloating = false

    mainElement.addClass("mdl-textfield--expandable")
    document.createElement("label").apply {
      val l = this as HTMLLabelElement
      l.initContents()
      l.addClass("mdl-button", "mdl-js-button", "mdl-button--icon")
      l.htmlFor = _id
      mainElement.insertBefore(this, null)
    }
    document.createElement("div").apply {
      addClass("mdl-textfield__expandable-holder")
      insertBefore(input, null)
      insertBefore(label, null)
      insertBefore(error, null)
      label.htmlFor = _id
      mainElement.insertBefore(this, null)
    }
    return label
  }
}