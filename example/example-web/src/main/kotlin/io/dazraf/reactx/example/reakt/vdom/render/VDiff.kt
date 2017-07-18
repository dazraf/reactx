package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.log
import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import org.w3c.dom.*

object VDiff {
  private val KEY_ATTR = "key"

  fun diff(lhsParent: Node, rhsParent: VNode<*>): List<VPatch> {

    log.debug("diff", lhsParent, rhsParent)
    val patches = mutableListOf<VPatch>()
    diff(lhsParent, rhsParent, patches)
    return patches
  }

  fun diff(lhsParent: Node,
           rhsParent: VNode<*>,
           patches: MutableList<VPatch>) {
    log.debug("diff", lhsParent, rhsParent, patches)
    val rhsKeyMap = rhsParent.childNodes
      .filter { it is VElement && it.hasKey() }
      .map { it as VElement<*> }
      .map { it.key to it}
      .distinctBy { it.first }
      .toMap()

    val lhsKeyMap = lhsParent.childNodes.asSequence()
      .filter { it.hasKey() }
      .map { it as Element }
      .map { it.key() to it }
      .distinctBy { it.first }
      .toMap()
    log.debug("lhs keys:", lhsKeyMap.keys.joinToString(","))
    log.debug("rhs keys: ", rhsKeyMap.keys.joinToString(","))

    val processedKeys by lazy { mutableSetOf<String>() }

    generateSequence(lhsParent.firstChild to rhsParent.firstChild) {
      val next = dispatch(it.first, it.second, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
      log.debug("next", next)
      next
    }.takeWhile { it.first != null || it.second != null }.forEach {  }
  }

  @Suppress("UNCHECKED_CAST")
  private fun dispatch(lhs: Node?,
                       rhs: VNode<*>?,
                       lhsParent: Node,
                       patches: MutableList<VPatch>,
                       processedKeys: MutableSet<String>,
                       lhsKeyMap: Map<String, Element>,
                       rhsKeyMap: Map<String, VElement<*>>): Pair<Node?, VNode<*>?> {
    log.debug("dispatch", lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
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
      return processElements(lhs, rhs as VElement<Element>, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
    }
    return replace(lhs, rhs, lhsParent, patches, processedKeys)
  }

  private fun processElements(lhs: Element,
                              rhs: VElement<Element>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              processedKeys: MutableSet<String>,
                              lhsKeyMap: Map<String, Element>,
                              rhsKeyMap: Map<String, VElement<*>>) : Pair<Node?, VNode<*>?> {
    log.debug("processElements", lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)

    if (rhs.hasKey()) {
      if (lhs.hasKey()) { // both sides have keys
        return processBothKeyed(lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
      } else { // only rhs has a key
        return processRhsKeyed(lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap)
      }
    }
    else if (lhs.hasKey()) { // only lhs has a key
      return processLhsKeyed(lhs, rhs, lhsParent, patches, rhsKeyMap)
    }
    else if (lhs.tagName == rhs.tagName) {
      patches += PushProps(rhs, lhs)
      diff(lhs, rhs, patches)
      return lhs.nextSibling to rhs.nextSibling
    }
    else {
      return replace(lhs, rhs, lhsParent, patches, processedKeys)
    }
  }

  private fun processLhsKeyed(lhs: Element,
                              rhs: VElement<*>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              rhsKeyMap: Map<String, VElement<*>>): Pair<Node?, VNode<*>?> {
    log.debug("processLhsKeyed", lhs, rhs, lhsParent, patches, rhsKeyMap)
    if (rhsKeyMap.containsKey(lhs.key())) {
      patches += InsertVNodeBefore(rhs, lhs, lhsParent)
    } else {
      patches += ReplaceNode(rhs, lhs, lhsParent)
    }
    return lhs.nextSibling to rhs.nextSibling
  }

  private fun processRhsKeyed(lhs: Element,
                              rhs: VElement<*>,
                              lhsParent: Node,
                              patches: MutableList<VPatch>,
                              processedKeys: MutableSet<String>,
                              lhsKeyMap: Map<String, Element>): Pair<Node?, VNode<*>?> {
    log.debug("processRhsKeyed", lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap)

    if (lhsKeyMap.containsKey(rhs.key)) {
      patches += InsertNodeBefore(lhsKeyMap[rhs.key]!!, lhs, lhsParent)
      patches += RemoveNode(lhs, lhsParent )
    } else {
      patches += ReplaceNode(rhs, lhs, lhsParent)
    }
    trackKey(rhs, processedKeys)
    return lhs.nextSibling to rhs.nextSibling
  }

  private fun processBothKeyed(lhs: Element,
                               rhs: VElement<*>,
                               lhsParent: Node,
                               patches: MutableList<VPatch>,
                               processedKeys: MutableSet<String>,
                               lhsKeyMap: Map<String, Element>,
                               rhsKeyMap: Map<String, VElement<*>>): Pair<Node?, VNode<*>?> {
    log.debug("processBothKeyed", lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKeyMap)
    val lhsKey = lhs.key()
    val rhsKey = rhs.key

    // CASE: keys may be the same
    if (lhsKey == rhsKey) {
      // do nothing the node is in the right place
      trackKey(rhs, processedKeys)
      return lhs.nextSibling to rhs.nextSibling
    }
    // CASE: LHS has already been processed, so skip it
    else if (processedKeys.contains(lhsKey)) {
      // CASE: RHS has also been processed, so skip that too
      if (processedKeys.contains(rhsKey)) {
        return lhs.nextSibling to rhs.nextSibling
      } else {
        return lhs.nextSibling to rhs
      }
    }
    // CASE: lhs exists in the new rhs state
    else if (rhsKeyMap.containsKey(lhsKey)) { // the lhs node will be processed later
      return processBothKeyedLhsExistsInRhs(lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKey)
    }
    // CASE: lhs doesn't exist in rhs state
    else {
      return processBothKeyedLhsNotInRhs(lhs, rhs, lhsParent, patches, processedKeys)
    }
  }

  private fun processBothKeyedLhsNotInRhs(lhs: Element,
                                          rhs: VElement<*>,
                                          lhsParent: Node,
                                          patches: MutableList<VPatch>,
                                          processedKeys: MutableSet<String>): Pair<Node?, VNode<*>?> {
    log.debug("processBothKeyedLhsNotInRhs", lhs, rhs, lhsParent, patches, processedKeys)
    patches += ReplaceNode(rhs, lhs, lhsParent)
    trackKey(rhs, processedKeys)
    return lhs.nextSibling to rhs.nextSibling
  }

  private fun processBothKeyedLhsExistsInRhs(lhs: Element, rhs: VElement<*>,
                                             lhsParent: Node,
                                             patches: MutableList<VPatch>,
                                             processedKeys: MutableSet<String>,
                                             lhsKeyMap: Map<String, Element>,
                                             rhsKey: String): Pair<Node?, VNode<out Node>?> {
    log.debug("processBothKeyedLhsExistsInRhs", lhs, rhs, lhsParent, patches, processedKeys, lhsKeyMap, rhsKey)
    // CASE: lhs has not been processed, is keyed, and present in rhs state. rhs node exists in lhs state
    if (lhsKeyMap.containsKey(rhsKey)) {
      patches += InsertNodeBefore(lhsKeyMap[rhsKey]!!, lhs, lhsParent)
    }
    // CASE: lhs has not been processed, is keyed, and present in rhs state. rhs node is new - it does not exist in lhs state
    else {
      patches += InsertVNodeBefore(rhs, lhs, lhsParent)
    }
    trackKey(rhs, processedKeys)
    trackKey(lhs, processedKeys)
    val result = lhs.nextSibling to rhs.nextSibling
    log.debug("processBothKeyedLhsExistsInRhs:result", result)
    return result
  }


  private fun replace(lhs: Node?,
                      rhs: VNode<*>?,
                      lhsParent: Node,
                      patches: MutableList<VPatch>,
                      keysProcessed: MutableSet<String>): Pair<Node?, VNode<*>?> {
    log.debug("replace", lhs, rhs, lhsParent, patches, keysProcessed)
    if (lhs != null && rhs != null) {
      patches += ReplaceNode(rhs, lhs, lhsParent)
      trackKey(rhs, keysProcessed)
      return lhs.nextSibling to rhs.nextSibling
    } else {
      error("lhs and rhs are both null")
    }
  }

  private fun removeLhs(lhs: Node?,
                        lhsParent: Node,
                        patches: MutableList<VPatch>): Node? {
    log.debug("removeLhs", lhs, lhsParent, patches)
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
    log.debug("addRhsAtEnd", rhs, lhsParent, patches, keysProcessed)
    if (rhs != null) {
      patches += InsertVNodeBefore(rhs, null, lhsParent)
      trackKey(rhs, keysProcessed)
      return rhs.nextSibling
    } else {
      error("rhs is null")
    }
  }

  private fun trackKey(r: VNode<*>, keysProcessed: MutableSet<String>) {
    if (r.hasKey()) keysProcessed.add(r.key())
  }

  private fun trackKey(r: Node, keysProcessed: MutableSet<String>) {
    if (r.hasKey()) keysProcessed.add(r.key())
  }
}

