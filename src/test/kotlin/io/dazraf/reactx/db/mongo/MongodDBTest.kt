package io.dazraf.reactx.db.mongo

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.litote.kmongo.async.KMongo
import org.litote.kmongo.*
import io.dazraf.reactx.db.mongo.*
import io.dazraf.reactx.getCollection
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.RunTestOnContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.Rule
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch
import kotlin.test.fail

data class Fruit(val name: String, val _id: String? = null)

@RunWith(VertxUnitRunner::class)
class MongodDBTest {
  @Rule
  @JvmField
  val rule = RunTestOnContext()
  val mongod by lazy {
    MongodDB("localhost", 12345, rule.vertx())
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(MongodDBTest::class.java)
  }

  @Before
  fun before() {
    mongod.start()
  }

  @After
  fun after() {
    mongod.stop()
  }

  @Test
  fun `that given insert we receive an event`(context: TestContext) {
    val async = context.async(2)
    val collection = mongod.getDatabase("testDB").getCollection<Fruit>("fruits")
    collection.events().subscribe {
      LOG.info("received event: $it")
      async.countDown()
    }
    collection.insert(Fruit("banana"))
      .doOnSuccess { LOG.info("inserted $it") }
      .flatMap { collection.find(Filters.eq("name", "blah")).first().toSingle() }
      .doOnSuccess { LOG.info("found $it") }
      .subscribe({ async.countDown()}, { context.fail(it) })
  }
}