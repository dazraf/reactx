package io.dazraf.reactx

import com.orientechnologies.orient.core.db.ODatabaseType
import com.orientechnologies.orient.core.db.OrientDB
import com.orientechnologies.orient.core.db.OrientDBConfig


val DB_NAME = "test"

fun main(args: Array<String>) {
  val orientDB = OrientDB("embedded:/", OrientDBConfig.defaultConfig())
  if (!orientDB.exists(DB_NAME)) {
    orientDB.create(DB_NAME, ODatabaseType.PLOCAL);
  }
  orientDB.open(DB_NAME, "admin", "admin").use { db ->
    // Enter your code here...
  }
  orientDB.close()
}

