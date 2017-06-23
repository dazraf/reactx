package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.log
import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
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
    node.parentNode?.removeChild(node) ?: log.error("could not find parent of", node)
  }
}

class ReplacePatch(val node: Node, val vNode: VNode<*>) : VPatch() {
  override fun apply() {
    node.parentNode?.replaceChild(vNode.render(), node) ?: log.error("could not find parent of", node)
  }
}

class MoveElementPatch(val newElement: Element, val oldElement: Element) : VPatch() {
  override fun apply() {

  }
}
class MoveNodePatch(val newNode: Node, val oldNode: Node) : VPatch() {
  override fun apply() {
    oldNode.parentNode?.replaceChild(newNode, oldNode) ?: log.error("could not find parent of", oldNode)
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
