package io.dazraf.reactx.example.mdl.components

import org.w3c.dom.Element

fun Element.icon(name: String) : Element {
  val icon = Icon(name)
  this.append(icon.mainElement)
  return icon.mainElement
}

fun MdlComponent.icon(name: String) : Element {
  return mainElement.icon(name)
}

class Icon(name: String) : MdlComponent("i", "material-icons") {
  init {
    this.htmlTextPram(name)
  }
}