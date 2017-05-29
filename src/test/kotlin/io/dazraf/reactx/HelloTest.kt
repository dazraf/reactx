package io.dazraf.reactx

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.RunTestOnContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import io.vertx.rx.java.RxHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rx.Observable
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit


@RunWith(VertxUnitRunner::class)
class HelloTest {

  data class Account(val id: String, val tx: String, val json: JsonObject = JsonObject(), val active: Boolean = true, val timestamp: ZonedDateTime = ZonedDateTime.now())
  @Rule
  @JvmField
  val rule = RunTestOnContext()

  @Before
  fun before() {
    val jtm = JavaTimeModule()
    Json.mapper.registerModule(jtm)
    Json.prettyMapper.registerModule(jtm)
  }

  @Test
  fun observe(context: TestContext) {
    val count = 10
    val async = context.async(count)
    val obs = Observable
      .interval(100, TimeUnit.MILLISECONDS, RxHelper.scheduler(rule.vertx()))
      .take(10)
      .map { Account(it.toString(), it.toString())}

    rule.vertx().createHttpServer()
      .websocketHandler { ws ->
        obs.subscribe {
          ws.writeFinalTextFrame(Json.encode(it))
        }
      }
      .listen(8080)

    rule.vertx().createHttpClient()
      .websocket(8080, "localhost", "/") { ws ->
        ws.handler {
          println(it.toString())
          async.countDown()
        }
      }
  }

}
