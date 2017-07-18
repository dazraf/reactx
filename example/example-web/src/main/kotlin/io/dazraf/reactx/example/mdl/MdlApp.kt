package io.dazraf.reactx.example.mdl

import io.dazraf.reactx.example.mdl.components.layout.navigationlayout.Layout
import io.dazraf.reactx.example.mdl.components.layout.navigationlayout.MdlContent
import org.w3c.dom.NavigatorID
import kotlin.browser.document

fun mdlApp(elementId: String = "MdlApp", init: MdlApp.() -> Unit): MdlApp {
  val app = MdlApp(elementId)
  app.init()
  return app
}

class MdlApp(val rootElementID: String) {
  private val app = document.getElementById(rootElementID)

  init {
    requireNotNull(app) { "No MldApp Element found!" }
  }

  fun navigationLayout(content: MdlContent, cssClass: String = "", init: Layout.() -> Unit) {
    val nl = Layout(content, cssClass)
    nl.init()
    nl.mainElement.append(nl.content.content.mainElement)
    app?.append(nl.mainElement)
  }
}
