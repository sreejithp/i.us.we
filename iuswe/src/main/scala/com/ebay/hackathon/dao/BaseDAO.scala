package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.traits.SerialisableEntity
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoCursorBase, TypeImports}
import com.mongodb.{DBObject, WriteConcern}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


trait DAO[ObjectType <: SerialisableEntity[ObjectType], ID <: Any] {

  def collection: MongoCollection

  lazy val description: String = "DAO"

  def insert(t: ObjectType): Option[ID]

  def insert(list: List[ObjectType])

  def insert(t: ObjectType, wc: WriteConcern): Option[ID]

  def group[A <% DBObject, B <% DBObject, C <% DBObject](key: A, cond: B, initial: C, reduceJSFn: String): List[ObjectType]

  def find[A <% DBObject](ref: A): CustomMongoCursor[ObjectType]

  def find[A <% DBObject, B <% DBObject](ref: A, keys: B): CustomMongoCursor[ObjectType]

  def findOne[A <% DBObject](t: A): Option[ObjectType]

  def findOneByID(id: ID): Option[ObjectType]

  def distinct[A <% DBObject](key: String, q: A): List[String]

  def save(t: ObjectType)

  def save(t: ObjectType, wc: WriteConcern)

  def update[A <% DBObject, B <% DBObject](q: A, o: B, upsert: Boolean, multi: Boolean, wc: WriteConcern)

  def update[A <% DBObject](q: A, o: ObjectType, upsert: Boolean, multi: Boolean, wc: WriteConcern)

  def remove(t: ObjectType)

  def remove(t: ObjectType, wc: WriteConcern)

  def remove[A <% DBObject](q: A)

  def remove[A <% DBObject](q: A, wc: WriteConcern)


}

abstract class BaseDAO[ObjectType <: SerialisableEntity[ObjectType], ID <: Any](val collection: MongoCollection, entityClass: Class[ObjectType])(implicit mot: Manifest[ObjectType], mid: Manifest[ID]) extends DAO[ObjectType, ID] {
  override lazy val description = "BaseDAO[%s,%s](%s)".format(mot.runtimeClass.getSimpleName, mid.runtimeClass.getSimpleName, collection.name)
  protected val FETCH_ONLY_ID = MongoDBObject("_is" -> 1)

  collection.setWriteConcern(WriteConcern.ACKNOWLEDGED)

  def insert(t: ObjectType) = insert(t, collection.writeConcern)

  def insert(list: List[ObjectType]) {
    if (list.length > 0) {
      val docs = (for (doc <- list) yield doc.asDBObject).asJava

      collection.underlying.insert(docs)
    }
  }

  def insert(t: ObjectType, wc: WriteConcern) = {
    val dbo = t.asDBObject
    val wr = collection.insert(dbo, wc)
    val error = wr.getCachedLastError
    if (error == null || (error != null && error.ok())) {
      Some(dbo.get("_id").asInstanceOf[ID])
    }
    else {
      throw CustomInsertError(description, collection, wc, wr, List(dbo))
    }
  }

  def group[A <% DBObject, B <% DBObject, C <% DBObject](key: A, cond: B, initial: C, reduceJSFn: String) = {
    val groupedResults = collection.group(key, cond, initial, reduceJSFn)
    groupedResults.map(entityClass.newInstance().fromDBObject(_)).toList
  }

  def aggregate[A <% DBObject](aggreateConfig: List[A]) = {
    val aggregateOutput = collection.aggregate(aggreateConfig)
    aggregateOutput.results.map(entityClass.newInstance().fromDBObject(_)).toList
  }

  def find[A <% DBObject, B <% DBObject](ref: A, keys: B) = {
    //    logQueries(ref)
    val find1: TypeImports.MongoCollection#CursorType = collection.find(ref, keys)

    CustomMongoCursor[ObjectType](entityClass,
      find1.asInstanceOf[MongoCursorBase].underlying)
  }


  val _LOGGER = LoggerFactory getLogger "BaseDAO"

  private def logQueries[A <% DBObject](ref: A) {
    val mongoQuery = collection.find(ref)
    _LOGGER.info(mongoQuery.query.toString)
    _LOGGER.info(mongoQuery.explain.toString)
  }

  def find[A <% DBObject](ref: A) = find(ref.asInstanceOf[DBObject], MongoDBObject.empty)

  def findOne[A <% DBObject](t: A) = collection.findOne(t).map(entityClass.newInstance().fromDBObject(_))

  def findOneByID(id: ID) = collection.findOneByID(id.asInstanceOf[AnyRef]).map(entityClass.newInstance().fromDBObject(_))

  def distinct[A <% DBObject](key: String, q: A): List[String] = {
    collection.distinct(key, q).map(_.toString).toList
  }

  def save(t: ObjectType) {
    save(t, collection.writeConcern)
  }

  def save(t: ObjectType, wc: WriteConcern) {
    val dbo = t.asDBObject
    val wr = collection.save(dbo, wc)
    val lastError = wr.getCachedLastError
    if (lastError != null && !lastError.ok()) {
      throw CustomSaveError(description, collection, wc, wr, List(dbo))
    }
  }

  def update[A <% DBObject, B <% DBObject](q: A, o: B, upsert: Boolean = false, multi: Boolean = false, wc: WriteConcern = collection.writeConcern) {
    val wr = collection.update(q, o, upsert, multi, wc)
    val lastError = wr.getCachedLastError
    if (lastError != null && !lastError.ok()) {
      throw CustomDAOUpdateError(description, collection, q, o, wc, wr, upsert, multi)
    }
  }

  def update[A <% DBObject](q: A, t: ObjectType, upsert: Boolean, multi: Boolean, wc: WriteConcern) {
    update(q, t.asDBObject, upsert, multi, wc)
  }

  def remove(t: ObjectType) {
    remove(t, collection.writeConcern)
  }

  def remove(t: ObjectType, wc: WriteConcern) {
    val dbo = t.asDBObject
    val wr = collection.remove(dbo, wc)
    val lastError = wr.getCachedLastError
    if (lastError != null && !lastError.ok()) {
      throw CustomRemoveError(description, collection, wc, wr, List(dbo))
    }
  }

  def remove[A <% DBObject](q: A) {
    remove(q, collection.writeConcern)
  }

  def remove[A <% DBObject](q: A, wc: WriteConcern) {
    val wr = collection.remove(q, wc)
    val lastError = wr.getCachedLastError
    if (lastError != null && !lastError.ok()) {
      throw CustomRemoveQueryError(description, collection, q, wc, wr)
    }
  }

}
