package io.dazraf.reactx.example.mdl.components.layout.navigationlayout

import io.dazraf.reactx.example.materialIcon
import io.dazraf.reactx.example.mdl.components.MdlComponent
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import kotlin.browser.document

interface LayoutNav {
  val mainElement: Element

  fun nav(cssClass: String = "", init: Nav.() -> Unit): Element {
    val nav = Nav(cssClass)
    nav.init()
    mainElement.append(nav.mainElement)
    return nav.mainElement
  }
}

class Nav(cssClassId: String = "") : MdlComponent("nav", "mdl-navigation", cssClassId) {

  fun link(init: Link.() -> Unit) {
    val link = Link()
    link.init()
    mainElement.append(link.mainElement)
    if (link.materialIcons.isNotEmpty()) link.mainElement.append(materialIcon(link.materialIcons))
    link.mainElement.append(document.createTextNode(link.text))
  }

  class Link(cssClassId: String = "") : MdlComponent("a", "mdl-navigation__link", cssClassId) {
    var href: String by htmlPram()

    var materialIcons: String = ""
    var text: String = ""

    fun onClick(doOn: () -> Unit) {
      (mainElement as HTMLElement).onclick = { doOn() }
    }
  }
}

