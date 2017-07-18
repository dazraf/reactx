package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import kotlin.dom.clear

abstract class VPatch {
  abstract fun apply()
}

class RemoveAllChildren(val node: Node) : VPatch() {
  override fun apply() {
    node.clear()
  }
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

class PushProps(val vElement: VElement<Element>, val element : Element): VPatch() {
  override fun apply() {
    vElement.pushProps(element)
  }
}

class InsertNodeAfterNode(val node: Node, val child: Node, val parent: Node) : VPatch() {
  override fun apply() {
     parent.insertBefore(node, child.nextSibling)
  }
}

class SwapNodes(val node1: Node, val node2: Node, val parent: Node): VPatch() {
  override fun apply() {
    val node1Sibling = node1.nextSibling
    val node1Parent = node1.parentNode
    val node2Sibling = node2.nextSibling
    val node2Parent = node2.parentNode
    if (node1Parent != null) {
      node1Parent.insertBefore(node2, node1Sibling)
    } else {
      node2Parent?.removeChild(node2)
    }
    if (node2Parent != null) {
      node2Parent.insertBefore(node1, node2Sibling)
    } else {
      node1Parent?.removeChild(node1)
    }
  }
}