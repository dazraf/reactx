package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.elements.VElement
import org.w3c.dom.Element

object Reakt {
  fun render(vElement: VElement, container: Element) {
    val changeSet = diffContainer(container, vElement)
//    console.log("change list:")
//    changeSet.forEach {
//      console.log(it)
//    }
    changeSet.forEach {
      it.apply()
    }
  }
}
