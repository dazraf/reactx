package io.dazraf.reactx.example.mdl.components

import org.w3c.dom.Element

fun MdlComponent.icon(name: String) : Element {
  val icon = Icon(name)
  mainElement.append(icon.mainElement)
  return icon.mainElement
}

class Icon(name: String) : MdlComponent("i", "material-icons") {
  init {
    this.htmlTextPram(name)
  }
}