
package io.dazraf.reactx.example.reakt.vdom.node

import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement

class VUnorderedList : VElement<HTMLUListElement>(tag = "ul") {
  override fun shouldNodeUpdate(node: HTMLUListElement): Boolean {
    return false
  }
}

class VListItem : VElement<HTMLLIElement>(tag = "li") {
  override fun shouldNodeUpdate(node: HTMLLIElement): Boolean {
    return false
  }
}

fun VElement<*>.ul(fn: VUnorderedList.() -> Unit) : VUnorderedList {
  val ul = VUnorderedList()
  appendChild(ul)
  ul.fn()
  return ul
}

fun VUnorderedList.li(fn: VListItem.() -> Unit) : VListItem {
  val li = VListItem()
  appendChild(li)
  li.fn()
  return li
}