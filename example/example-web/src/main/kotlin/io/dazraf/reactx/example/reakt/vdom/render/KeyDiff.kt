package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import io.dazraf.reactx.example.reakt.vdom.node.VText
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import kotlin.dom.isElement
import kotlin.dom.isText

object KeyDiff {
  fun diff(lhs: Node, rhs: VNode<*>) : List<VPatch> {
    val changes = mutableListOf<VPatch>()
    diff(lhs, rhs, changes)
    return changes
  }

  private fun diff(lhs: Node, rhs: VNode<*>, changes: MutableList<VPatch>) {
    if (lhs.isMixedType() || rhs.isMixedType()) throw RuntimeException("cannot mix keyed and non-keyed type in a list")
    if (rhs.childNodes.count() == 0) {
      changes += RemoveAllChildren(lhs)
      return
    }
    if (rhs.childNodes.first().hasKey()) {
      patchKeyedTree(lhs, rhs, changes)
    } else {
      patchTree(lhs, rhs, changes)
    }
  }

  private fun patchTree(lhs: Node, rhs: VNode<*>, changes: MutableList<VPatch>) {
    var lhsIndex = 0
    var rhsIndex = 0
    while (lhsIndex < lhs.childNodes.length || rhsIndex < rhs.childNodes.count()) {
      val newIndices = patchNodes(lhs, lhsIndex, rhs, rhsIndex, changes)
      lhsIndex = newIndices.first
      rhsIndex = newIndices.second
    }
  }

  fun patchNodes(lhs: Node, lhsIndex: Int, rhs: VNode<*>, rhsIndex: Int, changes: MutableList<VPatch>) : Pair<Int, Int> {
    if (lhsIndex >= lhs.childNodes.length) {
      changes += InsertVNodeBefore(rhs.childNodes[rhsIndex], null, lhs)
      return lhsIndex to rhsIndex + 1
    }
    if (rhsIndex >= rhs.childNodes.count()) {
      changes += RemoveNode(lhs.childNodes.item(lhsIndex)!!, lhs)
      return lhsIndex + 1 to rhsIndex
    }
    val rhsNode = rhs.childNodes[rhsIndex]
    val lhsNode = lhs.childNodes.item(lhsIndex)!!
    if (lhsNode.nodeType != rhsNode.nodeType) {
      changes += ReplaceNode(rhsNode, lhsNode, lhs)
      return lhsIndex + 1 to rhsIndex + 1
    }
    if (lhsNode.isText) {
      val rhsText = rhsNode as VText
      val lhsText = lhsNode as Text
      if (lhsText.textContent?:"" != rhsText.text) {
        changes += ChangeText(rhsText, lhsText)
        return lhsIndex + 1 to rhsIndex + 1
      }
      if (lhsText.isElement) {
        val rhsElement = rhsNode as VElement<*>
        val lhsElement = lhsNode as Element
        if (lhsElement.tagName != rhsElement.tagName) {
          changes += ReplaceNode(rhsElement, lhsElement, lhs)
          return lhsIndex + 1 to rhsIndex + 1
        }
        patchElementAttributes(lhsElement, rhsElement, changes)
        diff(lhsElement, rhsElement, changes)
        return lhsIndex + 1 to rhsIndex + 1
      }
    }
    return lhsIndex + 1 to rhsIndex + 1
  }

  private fun patchElementAttributes(lhsElement: Element, rhsElement: VElement<*>, changes: MutableList<VPatch>) {

  }

  private fun patchKeyedTree(lhs: Node, rhs: VNode<*>, changes: MutableList<VPatch>) {

  }
}