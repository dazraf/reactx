
package io.dazraf.reactx.example.reakt.vdom.elements

class VUnorderedList : VElement(tag = "ul")
class VListItem : VElement(tag = "li")

fun VElement.ul(fn: VUnorderedList.() -> Unit) : VUnorderedList {
  val ul = VUnorderedList()
  ul.fn()
  this.children.add(ul)
  ul.addListener(this::makeDirty)
  return ul
}

fun VElement.li(fn: VListItem.() -> Unit) : VListItem {
  val li = VListItem()
  li.fn()
  this.children.add(li)
  li.addListener(this::makeDirty)
  return li
}