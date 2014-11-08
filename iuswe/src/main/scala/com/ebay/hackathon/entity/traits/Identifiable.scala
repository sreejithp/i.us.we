package com.ebay.hackathon.entity.traits

import com.mongodb.DBObject
import org.bson.types.ObjectId


trait Identifiable[T] extends SerialisableEntity[T] {

  var id: String = null
  @transient var _id: ObjectId = null

  import Identifiable._

  abstract override def asDBObject: DBObject = {
    val dbObject = super.asDBObject
    if (_id != null) dbObject.put(ID, _id)
    dbObject
  }

  abstract override def fromDBObject(dbObject: DBObject) = {
    super.fromDBObject(dbObject)
    setId(getObjectId(dbObject, ID))
    this.asInstanceOf[T]
  }

  def setId(i: ObjectId) = {
    this._id = i
    this.id = this._id.toString
    this
  }

}

object Identifiable {
  val ID: String = "_id"
}





