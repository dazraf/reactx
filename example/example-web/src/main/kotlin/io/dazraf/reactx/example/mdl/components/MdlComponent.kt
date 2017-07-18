package io.dazraf.reactx.example.mdl.components

import io.dazraf.reactx.example.MdlColor
import io.dazraf.reactx.example.classType
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.appendText
import kotlin.dom.hasClass
import kotlin.dom.removeClass
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class MdlComponent(tag: String, classType: String, cssClassId: String = "") {
  val mainElement = document.createElement(tag).apply { this classType "$cssClassId $classType" }
  open var id: String
    get() = mainElement.id
    set(value) {
      mainElement.id = value
    }

  var backgroundColor: MdlColor.Background? = null
    set(value) {
      value?.let { mainElement.setAttribute("class", mainElement.getAttribute("class")?.plus(" $it")!!) }
    }

  var textColor: MdlColor.Text? = null
    set(value) {
      value?.let { mainElement.setAttribute("class", mainElement.getAttribute("class")?.plus(" $it")!!) }
    }

  fun classFlag(className: String) = object: ReadWriteProperty<Any, Boolean> {
    override operator fun getValue(thisRef: Any, property: KProperty<*>) : Boolean {
      return mainElement.hasClass(className)
    }
    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
      if (value && !mainElement.hasClass(className)) {
        mainElement.addClass(className)
      } else if (!value && mainElement.hasClass(className)) {
        mainElement.removeClass(className)
      }
    }
  }

  fun <T> htmlPram(parent: Element = mainElement): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    private var prop: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
      return prop ?: throw IllegalStateException("Property ${property.name} should be initialized before get.")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
      prop = value
      set(property.name, prop!!)
    }

    private fun set(name: String, value: T) {
      parent.setAttribute(name, "$value")
      if (name == "href") parent.setAttribute("target", "_blank")
    }
  }

  fun htmlTextPram(text: String = "", parent: Element = mainElement): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    private var prop: String = text

    init {
      set(prop)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String = prop

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
      prop = value
      set(prop)
    }

    private fun set(value: String) {
      parent.textContent = value
    }
  }

  fun <T : MdlComponent> appendToMain(initItem: T) = object : ReadWriteProperty<Any, T> {
    var item = initItem

    init {
      mainElement.append(item.mainElement)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = item

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
      mainElement.append(value.mainElement)
      item = value
    }
  }

  fun <T : MdlComponent> appendToMainIf(condition: Boolean, initItem: T) = object : ReadWriteProperty<Any, T> {
    var item = initItem

    init {
      if (condition) mainElement.append(item.mainElement)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = item

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
      if (condition) {
        mainElement.append(value.mainElement)
        item = value
      }
    }
  }

  operator fun String.unaryPlus() {
    mainElement.appendText(this)
  }

  operator fun Element.unaryPlus() {
    mainElement.append(this)
  }
}
