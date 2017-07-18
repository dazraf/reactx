package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.classType
import io.dazraf.reactx.example.plus
import io.dazraf.reactx.example.span
import org.w3c.dom.Element
import kotlin.browser.document

inline fun Element.list(cssClassId: String = "", init: List.() -> Unit): Element {
  val list = List(cssClassId)
  list.init()
  this + list.mainElement
  return list.mainElement
}

class List(cssClassId: String = "") : MdlComponent("ul", "mdl-list", cssClassId) {
  fun item(listItem: ListItem) {
    mainElement + document.createElement("li").apply {
      this classType "mdl-list__item mdl-list__item--three-line"
      span("mdl-list__item-text-body") { textContent = listItem.primaryText }
    }
  }
}

data class ListItem(val primaryText: String)