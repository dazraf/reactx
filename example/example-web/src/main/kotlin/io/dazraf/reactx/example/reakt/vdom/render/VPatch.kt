package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.elements.VElement
import io.dazraf.reactx.example.reakt.vdom.elements.VNode
import io.dazraf.reactx.example.reakt.vdom.elements.VText
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text

abstract class VPatch {
  abstract fun apply()
}

class AppendPatch(val node: Node, val vNode: VNode<*>) : VPatch() {
  override fun apply() {
    node.appendChild(vNode.render())
  }
}

class RemovePatch(val node: Node) : VPatch() {
  override fun apply() {
    node.parentNode?.removeChild(node)
  }
}

class ReplacePatch(val node: Node, val vNode: VNode<*>) : VPatch() {
  override fun apply() {
    node.parentNode?.replaceChild(vNode.render(), node)
  }
}

class PropsPatch<T : Element>(val element: T, val vElement: VElement<T>) : VPatch() {
  override fun apply() {
    vElement.pushProps(element)
  }
}

class TextPatch(val text: Text, val vText: VText) : VPatch() {
  override fun apply() {
    text.textContent = vText.text
  }

}
