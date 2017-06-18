package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
import io.dazraf.reactx.example.reakt.vdom.log
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import org.w3c.dom.get

fun diffContainer(container: Node, vContainer: VElement<*>, patches : MutableList<VPatch>) {
  // collect keyed vElements
  var index = 0
  var vIndex = 0
  while (index < container.childNodes.length || vIndex < vContainer.children.size) {
    val node = if (index < container.childNodes.length) container.childNodes[index] else null
    val vNode = if (vIndex < vContainer.children.size) vContainer.children[vIndex] else null
    diffNode(node, vNode, container, patches)
    ++index
    ++vIndex
  }
}

@Suppress("UNCHECKED_CAST")
fun diffNode(node: Node?, vNode: VNode<*>?, nodeParent: Node, patches: MutableList<VPatch>) {
  log.debug("diffNode", node, vNode)

  // remove the node if we don't have vNode
  if (vNode == null) {
    if (node != null) {
      log.debug("patch: remove", node)
      patches += RemovePatch(node)
    }
    return
  }

  if (node == null) {
    log.debug("patch: append", nodeParent, vNode)
    patches += AppendPatch(nodeParent, vNode)
  } else {
    if (node.nodeType != vNode.nodeType){
      log.debug("patch: replace", node, vNode)
      patches += ReplacePatch(node, vNode)
    } else if (vNode is VElement){ // same nodeType
      val element = node as Element
      diffElement(element, vNode, patches)
    } else if (vNode is VText) {
      val text = node as Text
      diffText(text, vNode, patches)
    } else {
      throw RuntimeException("Unknown VNode type ${vNode::class.js}")
    }
  }
}

@Suppress("UNCHECKED_CAST")
fun diffElement(element: Element, vElement: VElement<*>, patches: MutableList<VPatch>) {
  log.debug("diffElement", element, vElement)
  if (!element.tagName.equals(vElement.tag, true)) {
    log.debug("patch: replace", element, vElement)
    patches += ReplacePatch(element, vElement)
  } else {
    if (vElement.shouldNodeUpdate()) {
      log.debug("patch: props", element, vElement)
      patches += PropsPatch(element, vElement as VElement<Element>)
    }
    diffContainer(element, vElement, patches)
  }
}

fun diffText(text: Text, vText: VText, patches: MutableList<VPatch>) {
  log.debug("diffing", text, vText)
  if (text.textContent != vText.text) {
    log.debug("patch: text", text, vText)
    patches += TextPatch(text, vText)
  }
}
