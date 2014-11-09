package com.ebay.hackathon.entity

import com.ebay.hackathon.entity.traits.{Identifiable, SerialisableEntity}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.TypeImports._
import org.joda.time.DateTime


/**
 * Author sreejith on 09/11/14 5:02 AM.
 */

class FoodAvailability extends FoodAvailabilityCore[FoodAvailability]
with Identifiable[FoodAvailability]

class FoodAvailabilityCore[T] extends SerialisableEntity[T] {
  var userId: String = null
  var volunteerId: String = null
  var loc: List[Double] = Nil
  var address: String = null
  var count: Int = 0
  var status: Int = 0
  var date: DateTime = null

  import com.ebay.hackathon.entity.FoodAvailability._

  def asDBObject: DBObject = {
    val builder = MongoDBObject.newBuilder
    if (userId != null) builder += USER_ID -> userId
    if (volunteerId != null) builder += VOLUNTEER_ID -> volunteerId
    if (loc != Nil) builder += LOCATION -> loc
    if (address != null) builder += ADDRESS -> address
    builder += COUNT -> count
    builder += STATUS -> status
    builder += DATE -> date
    builder.result()
  }

  def fromDBObject(dbObject: DBObject) = {
    userId = getString(dbObject, USER_ID)
    volunteerId = getString(dbObject, VOLUNTEER_ID)
    address = getString(dbObject, ADDRESS)
    loc = getPrimitiveList[Double](dbObject, LOCATION)
    count = getInt(dbObject, COUNT)
    status = getInt(dbObject, STATUS)
    date = getDate(dbObject, DATE)
    this.asInstanceOf[T]
  }

}

object FoodAvailability {
  val USER_ID: String = "userId"
  val VOLUNTEER_ID: String = "volId"
  val LOCATION: String = "loc"
  val ADDRESS: String = "address"
  val COUNT: String = "count"
  val STATUS: String = "status"
  val DATE: String = "date"
}