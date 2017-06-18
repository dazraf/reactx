package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.Text
import kotlin.browser.document

class VText(var text: String) : VNode<Text>() {
  override val nodeType: Short
    get() = 3 // "Text"

  override fun createNode(): Text {
    return document.createTextNode(text)
  }
}
