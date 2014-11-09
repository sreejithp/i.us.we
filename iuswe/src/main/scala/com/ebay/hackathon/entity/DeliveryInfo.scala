package com.ebay.hackathon.entity

import com.ebay.hackathon.entity.traits.{SerialisableEntity, Identifiable}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.TypeImports._
import org.joda.time.DateTime

/**
 * Author sreejith on 09/11/14 6:00 AM.
 */
class DeliveryInfo  extends DeliveryInfoCore[DeliveryInfo]
with Identifiable[DeliveryInfo]

class DeliveryInfoCore [T] extends SerialisableEntity[T] {
  var name: String = null
  var address: String = null
  var comments:List[String] = null
  var volunteerId: String = null
  var needyId: String = null
  var contributorIds:List[String] = null
  var deliveryDate:DateTime= null
  var photosIds:List[String] = null

  import com.ebay.hackathon.entity.DeliveryInfo._

  def asDBObject: DBObject = {
    val builder = MongoDBObject.newBuilder
    if (name != null) builder += NAME -> name
    if (address != null) builder += ADDRESS -> address
    if (comments != null) builder += COMMENTS -> comments
    if (volunteerId != null) builder += VOLUNTEER_ID -> volunteerId
    if (needyId != null) builder += NEEDY_ID -> needyId
    if (contributorIds != null) builder += CONTRIBUTOR_IDS -> contributorIds
    if (photosIds != null) builder += PHOTO_IDS -> photosIds
    if (deliveryDate != null) builder += DELIVERY_DATE -> deliveryDate
    builder.result()
  }

  def fromDBObject(dbObject: DBObject) = {
    name = getString(dbObject, NAME)
    address = getString(dbObject, ADDRESS)
    comments = getPrimitiveList[String](dbObject, COMMENTS)
    volunteerId = getString(dbObject, volunteerId)
    needyId = getString(dbObject, NEEDY_ID)
    deliveryDate = getDate(dbObject, DELIVERY_DATE)
    contributorIds = getPrimitiveList[String](dbObject, CONTRIBUTOR_IDS)
    photosIds = getPrimitiveList[String](dbObject, PHOTO_IDS)
    this.asInstanceOf[T]
  }
}

object DeliveryInfo {
  val NAME: String = "name"
  val ADDRESS: String = "address"
  val COMMENTS: String = "comments"
  val VOLUNTEER_ID: String = "volunteerId"
  val NEEDY_ID: String = "needyId"
  val CONTRIBUTOR_IDS: String = "contributorIds"
  val DELIVERY_DATE: String = "deliveryDate"
  val PHOTO_IDS: String = "photoIds"

}