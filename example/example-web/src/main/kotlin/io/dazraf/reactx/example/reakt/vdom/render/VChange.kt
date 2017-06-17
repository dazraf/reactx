package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.elements.VElement
import org.w3c.dom.Element

abstract class VChange {
  abstract fun apply()
}

class InsertBeforeBegin(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    element.insertAdjacentElement("beforebegin", vElement.render())
  }
}

class InsertAfterBegin(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    element.insertAdjacentElement("afterbegin", vElement.render())
  }
}

class InsertBeforeEnd(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    element.insertAdjacentElement("beforeend", vElement.render())
  }
}

class InsertAfterEnd(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    element.insertAdjacentElement("afterend", vElement.render())
  }
}

class Remove(val element: Element) : VChange() {
  override fun apply() {
    element.parentElement?.removeChild(element)
  }
}

class Replace(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    element.parentElement?.replaceChild(vElement.render(), element)
  }
}

class PushProps(val element: Element, val vElement: VElement) : VChange() {
  override fun apply() {
    vElement.pushProps(element)
  }
}

class PushText(val element: Element, val text: String) : VChange() {
  override fun apply() {
    element.textContent = text
  }

}
