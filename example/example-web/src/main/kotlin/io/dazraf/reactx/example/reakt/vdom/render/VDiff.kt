package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
import io.dazraf.reactx.example.reakt.vdom.log
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import org.w3c.dom.get
import kotlin.dom.isElement

private val result  = 3

fun diffContainer(container: Node, vContainer: VElement<*>, patches: MutableList<VPatch>) {
  // collect keyed vElements
  var index = 0
  var vIndex = 0
  val keyed = groupNodesByKey(container)
  while (index < container.childNodes.length || vIndex < vContainer.children.size) {
    val node = if (index < container.childNodes.length) container.childNodes[index] else null
    val vNode = if (vIndex < vContainer.children.size) vContainer.children[vIndex] else null
    diffNode(node, vNode, container, keyed, patches)
    ++index
    ++vIndex
  }
}

private fun groupNodesByKey(container: Node): Map<String, Element> {
  val result = mutableMapOf<String, Element>()
  val children = container.childNodes

  repeat(children.length) {
    val child = children[it]
    if (child != null && child.isElement) {
      val element = child as Element
      val key = element.getAttribute(VElement.KEY_FIELD)
      if (key != null) {
        if (result.containsKey(key)) {
          throw RuntimeException("children with same key: $key")
        }
        result[key] = child
      }
    }
  }
  return result
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
private inline fun diffNode(node: Node?, vNode: VNode<*>?, nodeParent: Node, keyed: Map<String, Element>, patches: MutableList<VPatch>) {
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
    if (node.nodeType != vNode.nodeType) {
      log.debug("patch: replace", node, vNode)
      patches += ReplacePatch(node, vNode)
    } else if (vNode is VElement) { // same nodeType
      val element = node as Element
      diffElement(element, vNode as VElement<Element>, keyed, patches)
    } else if (vNode is VText) {
      val text = node as Text
      diffText(text, vNode, patches)
    } else {
      throw RuntimeException("Unknown VNode type ${vNode::class.js}")
    }
  }
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
private inline fun diffElement(element: Element, vElement: VElement<Element>, keyed: Map<String, Element>, patches: MutableList<VPatch>) {
  log.debug("diffElement", element, vElement)
  if (vElement.hasKey()) {
    diffKeyedElement(element, vElement, keyed, patches)
  } else {
    if (!element.tagName.equals(vElement.tag, true)) {
      log.debug("patch: replace", element, vElement)
      patches += ReplacePatch(element, vElement)
    } else {
      if (vElement.shouldNodeUpdate(element)) {
        log.debug("patch: props", element, vElement)
        patches += PropsPatch(element, vElement)
      }
      diffContainer(element, vElement, patches)
    }
  }
}

fun diffKeyedElement(element: Element, vElement: VElement<Element>, keyed: Map<String, Element>, patches: MutableList<VPatch>) {
  val actualElement = keyed[vElement.key]!!
  diffElement(actualElement, vElement, keyed, patches)
  // the actual element may be the same as passed in element
  if (element != actualElement) {
    patchKeyedElement(patches, actualElement, element)
  }
}

private fun patchKeyedElement(patches: MutableList<VPatch>, actualElement: Element, element: Element) {
  patches += MoveNodePatch(actualElement, element)
}


@Suppress("NOTHING_TO_INLINE")
private inline fun diffText(text: Text, vText: VText, patches: MutableList<VPatch>) {
  log.debug("diffing", text, vText)
  if (text.textContent != vText.text) {
    log.debug("patch: text", text, vText)
    patches += TextPatch(text, vText)
  }
}
