package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.example.reakt.vdom.elements.VElement
import io.dazraf.reactx.example.reakt.vdom.log
import org.w3c.dom.Element

object Reakt {
  fun render(vElement: VElement<*>, container: Element) {
    log.debug("rendering", vElement)
    val patches = mutableListOf<VPatch>()
    diffContainer(container, vElement, patches)
    log.debug("changes: ${patches.size}", patches)
    patches.forEach {
      it.apply()
    }
  }
}
