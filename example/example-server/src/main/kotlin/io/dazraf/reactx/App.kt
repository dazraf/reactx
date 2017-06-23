package io.dazraf.reactx

import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.rxjava.core.AbstractVerticle
import io.vertx.rxjava.core.Vertx
import io.vertx.rxjava.ext.web.Router
import io.vertx.rxjava.ext.web.handler.StaticHandler
import org.litote.kmongo.async.json
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.jvmName

data class Name(val first: String, val last: String)

class App : AbstractVerticle() {
  companion object {

    private val LOG = LoggerFactory.getLogger(App::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
      System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory")
      InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE)
      Vertx.vertx().deployVerticle(App::class.jvmName)
    }
  }

  override fun start(startFuture: Future<Void>?) {
    val router = Router.router(vertx)
    val appStatic = StaticHandler.create(".")
      .setCachingEnabled(false)
      .setCacheEntryTimeout(1)
      .setMaxCacheSize(1)

    val webjarsStatic = StaticHandler.create("META-INF/resources/webjars")
        .setCachingEnabled(false)
        .setCacheEntryTimeout(1)
        .setMaxCacheSize(1)

    router.get("/api/test").handler { it.response().end(Json.encode(Name("Fred", "Flintstone"))) }
    router.get("/webjars/*").handler(webjarsStatic::handle)
    router.get().handler(appStatic::handle)
    router.exceptionHandler {
      LOG.error("router error", it)
    }
    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080)
    LOG.info("Started on http://localhost:8080/example-web/index.html")

    startFuture?.complete() ?: LOG.error("startFuture is null")
  }
}

