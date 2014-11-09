package com.ebay.hackathon.entity

import com.ebay.hackathon.entity.traits.{SerialisableEntity, Identifiable}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.TypeImports._

/**
 * Author sreejith on 09/11/14 5:52 AM.
 */
class Needy  extends  NeedyCore[Needy]
with Identifiable[Needy]

class NeedyCore [T] extends SerialisableEntity[T] {
  var name: String = null
  var address: String = null
  var totalPeople: Int = 0
  var rating: Int = 0
  var loc: List[Double] = Nil
  var volunteers: List[String] = null
  var approvalStatus: Int = 0

  import com.ebay.hackathon.entity.Needy._

  def asDBObject: DBObject = {
    val builder = MongoDBObject.newBuilder
    if (name != null) builder += NAME -> name
    if (address != null) builder += ADDRESS -> address
    if (volunteers != null) builder += VOLUNTEERS -> volunteers
    builder += TOTAL_PEOPLE -> totalPeople
    builder += APPROVAL_STATUS -> approvalStatus
    builder += RATING -> rating
    if (loc != Nil) builder += LOCATION -> loc
    builder.result()
  }

  def fromDBObject(dbObject: DBObject) = {
    name = getString(dbObject, NAME)
    address = getString(dbObject, ADDRESS)
    rating = getInt(dbObject, RATING)
    totalPeople = getInt(dbObject, TOTAL_PEOPLE)
    loc = getPrimitiveList[Double](dbObject, LOCATION)
    volunteers = getPrimitiveList[String](dbObject, VOLUNTEERS)
    approvalStatus = getInt(dbObject, APPROVAL_STATUS)
    this.asInstanceOf[T]
  }
}

object Needy {
  val NAME: String = "name"
  val RATING: String = "rating"
  val LOCATION: String = "loc"
  val ADDRESS: String = "address"
  val TOTAL_PEOPLE: String = "totalPeople"
  val VOLUNTEERS: String = "volunteers"
  val APPROVAL_STATUS: String = "approvalStatus"

}