package io.dazraf.reactx.db.mongo

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import de.flapdoodle.embed.mongo.Command
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.*
import de.flapdoodle.embed.mongo.distribution.Version.Main
import de.flapdoodle.embed.process.config.io.ProcessOutput
import de.flapdoodle.embed.process.io.Processors
import de.flapdoodle.embed.process.io.Slf4jLevel
import de.flapdoodle.embed.process.io.progress.Slf4jProgressListener
import de.flapdoodle.embed.process.runtime.Network
import io.dazraf.reactx.Database
import io.vertx.core.Vertx
import org.litote.kmongo.async.KMongo
import org.slf4j.LoggerFactory

class MongodDB(val bindIP: String, val port: Int, val vertx: Vertx ) {
  var mongodExecutable: MongodExecutable? = null
  var mongodProcess : MongodProcess? = null

  companion object {
    private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(MongodDB::class.java)
  }

  fun start(): MongodDB {
    val processOutput = ProcessOutput(
      Processors.logTo(LOG, Slf4jLevel.INFO),
      Processors.logTo(LOG, Slf4jLevel.ERROR),
      Processors.logTo(LOG, Slf4jLevel.TRACE))

    val runtimeConfig = RuntimeConfigBuilder()
      .defaultsWithLogger(Command.MongoD, LOG)
      .processOutput(processOutput)
      .artifactStore(ExtractedArtifactStoreBuilder()
        .defaults(Command.MongoD)
        .download(DownloadConfigBuilder()
          .defaultsForCommand(Command.MongoD)
          .progressListener(Slf4jProgressListener(LOG))))
      .build()

    val starter = MongodStarter.getInstance(runtimeConfig)

    val mongodConfig = MongodConfigBuilder()
      .version(Main.PRODUCTION)
      .net(Net(bindIP, port, Network.localhostIsIPv6()))
      .build()
    mongodExecutable = starter.prepare(mongodConfig)
    mongodProcess = mongodExecutable!!.start()

    return this
  }

  fun createMongoClient(): MongoClient {
    return MongoClients.create(KMongo.createClient("mongodb://$bindIP:$port"))
  }

  fun getDatabase(name: String) : Database {
    return DatabaseMongo(createMongoClient(), name, vertx)
  }

  fun stop(): MongodDB {
    mongodProcess?.stop()
    mongodExecutable?.stop()
    mongodProcess = null
    mongodExecutable = null
    return this
  }
}

