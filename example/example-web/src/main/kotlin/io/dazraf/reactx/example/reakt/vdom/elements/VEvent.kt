package io.dazraf.reactx.example.reakt.vdom.elements

import org.w3c.dom.events.Event

open class VEvent<out T : VElement<*>>(val target: T, val nativeEvent: Event)
open class VMouseEvent<out T : VElement<*>>(target: T, nativeEvent: Event) : VEvent<T>(target, nativeEvent)
