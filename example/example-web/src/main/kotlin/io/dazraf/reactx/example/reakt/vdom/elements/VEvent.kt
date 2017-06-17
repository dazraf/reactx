package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.events.Event

open class VEvent(target: VElement, nativeEvent: Event)
open class VMouseEvent(target: VElement, nativeEvent: Event) : VEvent(target, nativeEvent)
