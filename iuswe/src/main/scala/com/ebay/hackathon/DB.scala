package com.ebay.hackathon

import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.mongodb.casbah.{MongoClientURI, MongoClient}


/**
 * Created by sreejith on 08/11/14.
 */

object DB extends Logging {
  val connection = {
    val url = "mongodb://127.0.0.1"
    val db = "iuswe"
    LOGGER.info(s"Connecting to mongo at $url/$db")
    MongoClient(MongoClientURI(url))(db)
  }
  RegisterJodaTimeConversionHelpers()
}
