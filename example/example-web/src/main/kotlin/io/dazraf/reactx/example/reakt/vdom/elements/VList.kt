
package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement

class VUnorderedList : VElement<HTMLUListElement>(tag = "ul")
class VListItem : VElement<HTMLLIElement>(tag = "li")

fun VElement<*>.ul(fn: VUnorderedList.() -> Unit) : VUnorderedList {
  val ul = VUnorderedList()
  children += ul
  ul.fn()
  return ul
}

fun VUnorderedList.li(fn: VListItem.() -> Unit) : VListItem {
  val li = VListItem()
  children += li
  li.fn()
  return li
}