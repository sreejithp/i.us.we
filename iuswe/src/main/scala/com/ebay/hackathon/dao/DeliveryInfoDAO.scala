package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.DeliveryInfo
import com.ebay.hackathon.entity.DeliveryInfo._
import com.ebay.hackathon.{DB, Logging}
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * Author sreejith on 09/11/14 6:25 AM.
 */
object DeliveryInfoDAO extends BaseDAO[DeliveryInfo, ObjectId](collection = DB.connection("deliveryInfo"), entityClass = classOf[DeliveryInfo])
with Logging {

  def addDeliveryInfo(deliveryInfo: DeliveryInfo): Unit = {
    insert(deliveryInfo)
  }

  def getContributorDeliveryInfo(contributorId: String) = {
    find(MongoDBObject(CONTRIBUTOR_IDS -> contributorId)).toList
  }

  def getVolunteerDeliveryInfo(volunteerId:String) = {
    find(MongoDBObject(VOLUNTEER_ID -> volunteerId)).toList
  }

  def getDeliveryInfoBasedOnDate(date:DateTime) = {
    find(MongoDBObject(DELIVERY_DATE -> date)).toList
  }

}
