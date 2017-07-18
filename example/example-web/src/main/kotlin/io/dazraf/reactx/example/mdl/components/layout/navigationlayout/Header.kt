package io.dazraf.reactx.example.mdl.components.layout.navigationlayout

import io.dazraf.reactx.example.mdl.components.MdlComponent
import io.dazraf.reactx.example.mdl.components.layout.LayoutTitle

class Header(cssClassId: String = "", transparent: Boolean = false) :
  MdlComponent("header", if (transparent) "mdl-layout__header--transparent" else "mdl-layout__header", cssClassId) {

  internal class HeaderRow : MdlComponent("div", "mdl-layout__header-row") {
    val layoutTitle by appendToMain(LayoutTitle())
  }

  private val headerRow by appendToMain(HeaderRow())
  var title: String by htmlTextPram(parent = headerRow.layoutTitle.mainElement)
}
