package io.dazraf.reactx

import org.w3c.xhr.XMLHttpRequest
import kotlin.js.Promise

class Invoker(val host: String = "localhost", val port: Int = 8080, val basePath : String = ""){
  fun get(path: String): Promise<String> {
    return Promise({ response, reject ->
      val request = XMLHttpRequest()
      request.onreadystatechange = {
        if (request.readyState == 4.toShort()) {
          if ((request.status / 100) == 2) {
//            console.log("received result", request)
            response(request.responseText)
          } else {
//            console.log("failed", request)
            reject(Throwable("${request.status}: ${request.statusText}"))
          }
        }
      }
      val fullPath = "http://$host:$port$basePath$path"
      console.log("GET $fullPath")
      request.open("GET", fullPath)
      request.send()
    })
  }
}

fun <T> Promise<String>.parse() : Promise<T> {
  return this.then({
    JSON.parse<T>(it)
  })
}