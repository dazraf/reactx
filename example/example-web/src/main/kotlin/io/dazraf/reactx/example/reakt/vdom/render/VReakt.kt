package io.dazraf.reactx.example.reakt.vdom.render

import io.dazraf.reactx.log

object Reakt {
  fun render(patches: List<VPatch>) {
    patches.forEach {
      try {
        it.apply()
      } catch (err: Throwable) {
        log.error("failed to execute patch", it)
      }
    }
  }
}
