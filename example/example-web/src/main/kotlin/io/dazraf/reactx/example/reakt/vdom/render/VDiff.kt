package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import org.w3c.dom.*

object VDiff {
  private val KEY_ATTR = "key"

  fun diff(lhsParent: Node, rhsParent: VNode<*>): List<VPatch> {
    val patches = mutableListOf<VPatch>()
    diff(lhsParent, rhsParent, patches)
    return patches
  }

  fun diff(lhsParent: Node,
           rhsParent: VNode<*>,
           patches: MutableList<VPatch>) {

    val rhsKeyMap = rhsParent.childNodes.filter { it is VElement && it.hasKey() }.groupBy { (it as VElement).key }
    val lhsKeyMap = lhsParent.childNodes.sequence().filter { it.hasKey() }.groupBy { it.key() }
    val processedKeys = mutableSetOf<String>()

    generateSequence(lhsParent.firstChild to rhsParent.firstChild) {
      dispatch(it.first, it.second, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
    }
  }

  private fun dispatch(lhs: Node?,
                       rhs: VNode<*>?,
                       lhsParent: Node,
                       patches: MutableList<VPatch>,
                       processedKeys: MutableSet<String>,
                       lhsKeyMap: Map<String, List<Node>>,
                       rhsKeyMap: Map<String, List<VNode<*>>>): Pair<Node?, VNode<*>?> {
    check(lhs != null || rhs != null)
    if (lhs == null) {
      val newRhs = addRhsAtEnd(rhs, lhsParent, patches, processedKeys)
      return lhs to newRhs
    }
    if (rhs == null) {
      val newLhs = removeLhs(lhs, lhsParent, patches)
      return newLhs to rhs
    }
    if (lhs is Element && rhs is VElement) {
      return processElements(lhs, rhs, lhsParent, patches, processedKeys)
    }
    return replace(lhs, rhs, lhsParent, patches, processedKeys)
  }

  private fun processElements(lhs: Element,
                              rhs: VElement<*>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              processedKeys: MutableSet<String>) : Pair<Node?, VNode<*>?> {

    diff(lhs, rhs, patches)
    if (rhs.hasKey()) {
      if (lhs.hasKey()) { // both sides have keys
        return processBothKeyed(lhs, rhs, lhsParent, patches, processedKeys)
      } else { // only rhs has a key
        return processRhsKeyed(lhs, rhs, lhsParent, patches, processedKeys)
      }
    } else if (lhs.hasKey()) { // only lhs has a key
      return processLhsKeyed(lhs, rhs, lhsParent, patches, processedKeys)
    } else {
      return replace(lhs, rhs, lhsParent, patches, processedKeys)
    }
  }

  private fun processLhsKeyed(lhs: Element,
                              rhs: VElement<*>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              processedKeys: MutableSet<String>): Pair<Node?, VNode<*>?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun processRhsKeyed(lhs: Element,
                              rhs: VElement<*>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              processedKeys: MutableSet<String>): Pair<Node?, VNode<*>?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun processBothKeyed(lhs: Element,
                               rhs: VElement<*>,
                               lhsParent: Node,
                               patches: MutableList<VPatch>,
                               processedKeys: MutableSet<String>): Pair<Node?, VNode<*>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun replace(lhs: Node?,
                      rhs: VNode<*>?,
                      lhsParent: Node,
                      patches: MutableList<VPatch>,
                      keysProcessed: MutableSet<String>): Pair<Node?, VNode<*>?> {
    if (lhs != null && rhs != null) {
      patches += ReplaceNode(rhs, lhs, lhsParent)
      trackRhsKey(rhs, keysProcessed)
      return lhs.nextSibling to rhs.nextSibling
    } else {
      error("lhs and rhs are both null")
    }
  }

  private fun differentNodeTypes(lhs: Node, rhs: VNode<*>) = lhs.nodeType != rhs.nodeType

  private fun removeLhs(lhs: Node?,
                        lhsParent: Node,
                        patches: MutableList<VPatch>): Node? {
    if (lhs != null) {
      patches += RemoveNode(lhs, lhsParent)
      return lhs.nextSibling
    } else {
      error("lhs should not be null")
    }
  }

  private fun addRhsAtEnd(rhs: VNode<*>?,
                          lhsParent: Node,
                          patches: MutableList<VPatch>,
                          keysProcessed: MutableSet<String>): VNode<*>? {
    if (rhs != null) {
      patches += InsertVNodeBefore(rhs, null, lhsParent)
      trackRhsKey(rhs, keysProcessed)
      return rhs.nextSibling
    } else {
      error("rhs is null")
    }
  }

  private fun trackRhsKey(r: VNode<*>, keysProcessed: MutableSet<String>) {
    if (r.hasKey()) keysProcessed.add(r.key())
  }

  private fun NodeList.sequence(): Sequence<Node> {
    val parent = this
    return object : Sequence<Node> {
      override fun iterator(): Iterator<Node> {
        return object : Iterator<Node> {
          var n = 0
          override fun hasNext() = n < parent.length
          override fun next(): Node {
            return parent[n++]!!
          }
        }
      }
    }
  }

  private fun Node?.isEOL() = this == null
  private fun VNode<*>?.isEOL() = this == null
  private fun Node.hasKey() = this is Element && this.hasAttribute(KEY_ATTR)
  private fun Node.key() = (this as Element).getAttribute(KEY_ATTR)!!
  private fun VNode<*>.hasKey() = this is VElement<*> && this.hasKey()
  private fun VNode<*>.key() = (this as VElement<*>).key
}

