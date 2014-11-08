package com.ebay.hackathon.dao

import com.mongodb.{DBObject, WriteConcern}
import com.mongodb.casbah.TypeImports._

abstract class CustomDAOError(whichDAO: String,
                              thingThatFailed: String,
                              collection: MongoCollection,
                              wc: WriteConcern,
                              wr: WriteResult,
                              dbos: List[DBObject]) extends Error("""

    %s: %s failed!

    Collection: %s
    WriteConcern: %s
    WriteResult: %s

    FAILED TO %s %s
    %s


 """.format(whichDAO, thingThatFailed,
  collection.name, wc, wr,
  thingThatFailed.toUpperCase(),
  if (dbos.size == 1) "DBO" else "DBOs",
  if (dbos.size == 1) dbos.head else dbos.mkString("\n")))

case class CustomInsertError(description: String,
                             collection: MongoCollection,
                             wc: WriteConcern,
                             wr: WriteResult,
                             dbos: List[DBObject]) extends CustomDAOError(description, "insert", collection, wc, wr, dbos)

case class CustomRemoveError(description: String,
                             collection: MongoCollection,
                             wc: WriteConcern,
                             wr: WriteResult,
                             dbos: List[DBObject]) extends CustomDAOError(description, "remove", collection, wc, wr, dbos)

case class CustomSaveError(description: String,
                           collection: MongoCollection,
                           wc: WriteConcern,
                           wr: WriteResult,
                           dbos: List[DBObject]) extends CustomDAOError(description, "save", collection, wc, wr, dbos)

abstract class CustomDAOQueryError(whichDAO: String,
                                   thingThatFailed: String,
                                   collection: MongoCollection,
                                   query: DBObject,
                                   wc: WriteConcern,
                                   wr: WriteResult) extends Error("""

    %s: %s failed!

    Collection: %s
    WriteConcern: %s
    WriteResult: %s

    QUERY: %s

 """.format(whichDAO, thingThatFailed, collection.name, wc, wr, query))

case class CustomRemoveQueryError(whichDAO: String,
                                  collection: MongoCollection,
                                  query: DBObject,
                                  wc: WriteConcern,
                                  wr: WriteResult) extends CustomDAOQueryError(whichDAO, "remove", collection, query, wc, wr)

case class CustomDAOUpdateError(whichDAO: String,
                                collection: MongoCollection,
                                query: DBObject,
                                o: DBObject,
                                wc: WriteConcern,
                                wr: WriteResult,
                                upsert: Boolean,
                                multi: Boolean) extends Error("""

    %s: update failed!

    Collection: %s
    WriteConcern: %s
    WriteResult: %s
    Upsert: %s
    Multi: %s

    QUERY: %s

    OBJECT TO UPDATE: %s

 """.format(whichDAO, collection.name, wc, wr, upsert, multi, query, o))
