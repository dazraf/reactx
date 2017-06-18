package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.HTMLImageElement

class VImage : VElement<HTMLImageElement>(tag = "img") {
  var src = ""

  override fun pushProps(element: HTMLImageElement) {
    super.pushProps(element)
    element.src = src
  }
}

fun VElement<*>.img(fn: VImage.() -> Unit) : VImage {
  val img = VImage()
  this.children += img
  img.fn()
  return img
}
