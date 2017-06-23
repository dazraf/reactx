package io.dazraf.reactx.example.reakt.vdom.node

import org.w3c.dom.Node

abstract class VNode<H : Node>(val children: MutableList<VNode<*>> = mutableListOf()) {
  open fun render() : H {
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

  protected abstract fun createNode() : H
}