package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
import org.w3c.dom.Node
import org.w3c.dom.Text

abstract class VPatch {
  abstract fun apply()
}

class RemoveNode(val node: Node, val parent: Node) : VPatch() {
  override fun apply() {
    parent.removeChild(node)
  }
}

class InsertVNodeBefore(val vnode: VNode<*>, val child: Node?, val parent: Node) : VPatch() {
  override fun apply() {
    parent.insertBefore(vnode.render(), child)
  }
}

class InsertNodeBefore(val node: Node, val child: Node?, val parent: Node) : VPatch() {
  override fun apply() {
    parent.insertBefore(node, child)
  }
}

class ChangeText(val textVNode: VText, val textNode: Text) : VPatch() {
  override fun apply() {
    textNode.textContent = textVNode.text
  }
}

class ReplaceNode(val vNode: VNode<*>, val child: Node, val parent: Node) : VPatch() {
  override fun apply() {
    parent.replaceChild(vNode.render(), child)
  }
}
