package io.dazraf.reactx.db.mongo

import com.mongodb.client.model.Filters
import io.dazraf.reactx.getCollection
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.RunTestOnContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import rx.Observable

data class Fruit(val name: String, val _id: String? = null)

@RunWith(VertxUnitRunner::class)
class MongodDBTest {
  @Rule
  @JvmField
  val rule = RunTestOnContext()
  val mongod = MongodDB("localhost", 12345)

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
  fun `that given writes operations we receive appropriate events`(context: TestContext) {
    val async = context.async(3)
    val collection = mongod.getDatabase("testDB", rule.vertx()).getCollection<Fruit>("fruits")

    val banana = Fruit("banana")
    val apple = Fruit("apple")


    collection.events().map { it.value }.zipWith(Observable.just(banana, apple), { first, second -> first to second})
      .subscribe {
        LOG.info("received event: $it")
        context.assertEquals(it.second, it.first, "should be the same")
        async.countDown()
      }

    collection.insert(banana)
      .doOnSuccess { LOG.info("inserted $it") }
      .flatMap { collection.findOne(Filters.eq("name", "banana"))}
      .doOnSuccess { LOG.info("found $it") }
      .flatMap { collection.updateOne(Filters.eq("_id", it._id), apple) }
      .doOnSuccess { LOG.info("updated $it") }
      .flatMap { collection.findOne(Filters.eq("name", "apple"))}
      .doOnSuccess { LOG.info("found $it") }
      .subscribe({ async.countDown()}, { context.fail(it) })
  }


}