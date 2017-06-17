package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.elements.VElement
import org.w3c.dom.Element
import org.w3c.dom.get

fun diffContainer(container: Element, vContainer: VElement) : List<VChange> {
  // collect keyed vElements
  val changes = mutableListOf<VChange>()

  var index = 0
  var vIndex = 0
  while (index < container.children.length || vIndex < vContainer.children.size) {
    val element = if (index < container.children.length) container.children[index] else null
    val vElement = if (vIndex < vContainer.children.size) vContainer.children[vIndex] else null
    val delta = diffElement(element, vElement, container)
    changes.addAll(delta)
    ++index
    ++vIndex
  }
  return changes
}

fun List<VElement>.byKey() : Map<String, VElement> = this.filter { it.key.isNotEmpty() }.associateBy { it.key }


fun diffElement(domElement: Element?, vElement: VElement?, domParent: Element): List<VChange> {
  val changes = mutableListOf<VChange>()
  if (vElement == null) {
    if (domElement != null) {
      changes += Remove(domElement)
    }
    return changes
  }
  if (domElement == null) {
    changes += InsertBeforeEnd(domParent, vElement)
  } else if (!domElement.tagName.equals(vElement.tag, true)) {
    changes += Replace(domElement, vElement)
  } else {
    if (vElement.shouldNodeUpdate()) {
      changes += PushProps(domElement, vElement)
    }
    changes.addAll(diffContainer(domElement, vElement))
  }

  return changes
}
