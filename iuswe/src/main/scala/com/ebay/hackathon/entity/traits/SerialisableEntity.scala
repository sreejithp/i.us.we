package com.ebay.hackathon.entity.traits

import com.mongodb.casbah.Imports._
import java.io.Serializable
import org.joda.time.DateTime
import com.mongodb.DBObject

/**
 * User: Sreejith
 * Date: 2/26/12
 */

trait BaseEntity {
  protected def getPrimitiveList[PrimitiveType](dbObject: DBObject, key: String): List[PrimitiveType] = {
    val dbList = dbObject.get(key)
    var result: List[PrimitiveType] = Nil
    if (dbList != null && dbList.isInstanceOf[BasicDBList]) {
      val valueList = dbList.asInstanceOf[BasicDBList].toArray.toList
      result = valueList.asInstanceOf[List[PrimitiveType]]
    }
    result
  }

  protected def getObjectList[ObjectType <: SerialisableEntity[ObjectType]](dbObject: DBObject, key: String, clazz: Class[ObjectType]) = {
    val dbList = dbObject.get(key)
    var result: List[ObjectType] = Nil
    if (dbList != null && dbList.isInstanceOf[BasicDBList]) {
      val valueList = dbList.asInstanceOf[BasicDBList].toArray.toList
      result = valueList.map((x) => clazz.newInstance().fromDBObject(x.asInstanceOf[DBObject]))
    }
    result
  }

  case class Init()

  import scala.language.implicitConversions
  implicit def initToT[T](i: Init): T = {
    null.asInstanceOf[T]
  }

  protected def getObject[ObjectType <: SerialisableEntity[ObjectType]](dbObject: DBObject, key: String, clazz: Class[ObjectType]) = {
    val dbValue = dbObject.get(key)
    var result: ObjectType = Init()
    if (dbValue != null && dbValue.isInstanceOf[DBObject]) {
      result = clazz.newInstance().fromDBObject(dbValue.asInstanceOf[DBObject])
    }
    result
  }

  protected def getObjectOption[ObjectType <: SerialisableEntity[ObjectType]](dbObject: DBObject, key: String, clazz: Class[ObjectType]) = {
    if(dbObject.get(key) == null)
      None
    else
      Some(getObject(dbObject, key, clazz))
  }

  protected def getString(dbObject: DBObject, key: String): String = {
    val dbValue = dbObject.get(key)
    var result: String = Init()
    if (dbValue != null && dbValue.isInstanceOf[String]) result = dbValue.asInstanceOf[String]
    result
  }

  protected def getInt(dbObject: DBObject, key: String): Int = {
    val dbValue = dbObject.get(key)
    var result: Int = 0
    if (dbValue != null && dbValue.isInstanceOf[Int]) result = dbValue.asInstanceOf[Int]
    result
  }

  protected def getDouble(dbObject: DBObject, key: String): Double = {
    val dbValue = dbObject.get(key)
    var result: Double = 0
    if (dbValue != null && dbValue.isInstanceOf[Double]) result = dbValue.asInstanceOf[Double]
    if (dbValue != null && dbValue.isInstanceOf[Int]) result = dbValue.asInstanceOf[Int].toDouble
    result
  }

  protected def getDate(dbObject: DBObject, key: String): DateTime = {
    val dbValue = dbObject.get(key)
    var result: DateTime = null
    if (dbValue != null && dbValue.isInstanceOf[DateTime]) result = dbValue.asInstanceOf[DateTime]
    result
  }

  protected def getObjectId(dbObject: DBObject, key: String): ObjectId = {
    val dbValue = dbObject.get(key)
    var result: ObjectId = null
    if (dbValue != null && dbValue.isInstanceOf[ObjectId]) result = dbValue.asInstanceOf[ObjectId]
    result
  }

  protected def getObjectIdOption(dbObject: DBObject, key: String): Option[ObjectId] = {
    val dbValue = dbObject.get(key)
    if (dbValue != null && dbValue.isInstanceOf[ObjectId]) Some(dbValue.asInstanceOf[ObjectId]) else None
  }

  protected def getBool(dbObject: DBObject, key: String): Boolean = {
    val dbValue = dbObject.get(key)
    var result: Boolean = false
    if (dbValue != null && dbValue.isInstanceOf[Boolean]) result = dbValue.asInstanceOf[Boolean]
    result
  }


}

trait SerialisableEntity[T] extends BaseEntity with Serializable {
  def asDBObject: DBObject
  def fromDBObject(obj: DBObject): T

}

