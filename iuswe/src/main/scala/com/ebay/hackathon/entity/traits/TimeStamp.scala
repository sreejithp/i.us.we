package com.ebay.hackathon.entity.traits

import com.mongodb.DBObject
import org.joda.time.DateTime


trait TimeStamp[T] extends SerialisableEntity[T] {


  var createdAgo: String = null
  var modifiedOnInMillis: java.lang.Long = 0
  var createdOnInMillis: java.lang.Long = 0

  var modifiedAgo: String = null
  var modifiedOnIso8601: String = null
  var createdOnIso8601: String = null

  @transient var createdOn: DateTime = null
  @transient var modifiedOn: DateTime = null

  import com.ebay.hackathon.entity.traits.TimeStamp._

  abstract override def asDBObject: DBObject = {
    val dbObject = super.asDBObject
    if (createdOn != null) dbObject.put(CREATED_ON, createdOn)
    if (modifiedOn != null) dbObject.put(MODIFIED_ON, modifiedOn)
    dbObject
  }

  abstract override def fromDBObject(dbObject: DBObject) = {
    super.fromDBObject(dbObject)

    this.modifiedOn = getDate(dbObject, MODIFIED_ON)
    this.createdOn = getDate(dbObject, CREATED_ON)

    computeTransitentFields

    this.asInstanceOf[T]
  }


  def computeTransitentFields {
    if (this.createdOn != null) {
      this.createdOnInMillis = this.createdOn.getMillis

      // If no known last modified, use creation date as modified
      if (this.modifiedOn == null) {
        this.modifiedOn = this.createdOn
      }
    }

    if (this.modifiedOn != null) {
      this.modifiedOnInMillis = this.modifiedOn.getMillis
    }
  }


}

object TimeStamp {
  val CREATED_ON: String = "createdOn"
  val MODIFIED_ON: String = "modifiedOn"
}
