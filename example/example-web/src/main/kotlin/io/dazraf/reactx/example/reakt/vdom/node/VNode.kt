package io.dazraf.reactx.example.reakt.vdom.node

import org.w3c.dom.Node

abstract class VNode<H : Node>(private val children: MutableList<VNode<*>> = mutableListOf()) {

  var parent: VNode<*>? = null
    get() = field
    protected set(value) {
      field = value
    }

  var nextSibling: VNode<*>? = null
    get() = field
    protected set(value) {
      field = value
    }

  var previousSibling: VNode<*>? = null
    get() = field
    protected set(value) {
      field = value
    }

  val childNodes: List<VNode<*>>
    get() = children

  open fun render(): H {
    val node = createNode()
    children.forEach {
      node.appendChild(it.render())
    }
    return node
  }

  open fun shouldNodeUpdate(node: H): Boolean {
    return true
  }

  abstract val nodeType: Short

  protected abstract fun createNode(): H

  fun appendChild(node: VNode<*>): VNode<H> {
    node.detach()
    node.previousSibling = children.lastOrNull()
    node.parent = this
    children.add(node)
    return this
  }

  fun insertBefore(node: VNode<*>, referenceNode: VNode<*>?): VNode<*> {
    node.detach()
    if (referenceNode == null) return appendChild(node)
    val index = children.indexOf(referenceNode)
    if (index < 0) throw IllegalArgumentException("cannot find reference element")
    val newChildren = children.slice(0..index - 1) + node + children.slice(index + 1..children.size - 1)
    children.clear()
    children.addAll(newChildren)

    if (index > 0) {
      children[index - 1].nextSibling = node
      node.previousSibling = children[index - 1]
    }
    if (index < children.size - 1) {
      children[index + 1].previousSibling = node
      node.nextSibling = children[index + 1]
    }
    node.parent = this
    return this
  }

  fun removeNode(node: VNode<*>): VNode<H> {
    node.detach()
    return this
  }

  fun detach() {
    previousSibling?.nextSibling = null
    nextSibling?.previousSibling = null
    parent?.children?.remove(this)
    nextSibling = null
    previousSibling = null
    parent = null
  }
}