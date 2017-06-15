package io.dazraf.reactx.example.reakt.vdom

import org.w3c.dom.Element

fun diff(containerNode: Element, vElements: List<VElement>) : List<VChange> {

}

fun diffElement(domElement: Element?, vElement: VElement, domParent: Element): List<VChange> {
  if (domElement == null) {
    return listOf(InsertAfterBegin(domParent, vElement))
  }

  if (domElement.tagName != vElement.tag) {
    return listOf(Replace(domElement, vElement))
  }

  if (vElement.shouldNodeUpdate()) {
    vElement.pushProps(domElement)
  }

  diffElementChildren(domElement, vElement)
  return domElement
}

fun diffElementChildren(domElement: Element, vElement: VElement) {

}

