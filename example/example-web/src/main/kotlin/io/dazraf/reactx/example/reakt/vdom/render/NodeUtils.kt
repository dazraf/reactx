package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.node.VElement
import io.dazraf.reactx.example.reakt.vdom.node.VNode
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.get

fun NodeList.asSequence(): Sequence<Node> {
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
fun Node.hasKey() = this is Element && this.hasAttribute("key")
fun Node.key() = (this as Element).getAttribute("key")!!
fun VNode<*>.hasKey() = this is VElement<*> && this.hasKey()
fun VNode<*>.key() = (this as VElement<*>).key

fun Node.isMixedType() : Boolean {
  val hasKey = this.hasKey()
  val firstNonMatch = this.childNodes.asSequence().drop(1).filter { it.hasKey() != hasKey}.firstOrNull()
  return firstNonMatch != null
}

fun VNode<*>.isMixedType() : Boolean {
  val hasKey = this.hasKey()

  val firstNonMatch = this.childNodes.asSequence().drop(1).filter { it.hasKey() != hasKey}.firstOrNull()
  return firstNonMatch != null
}

