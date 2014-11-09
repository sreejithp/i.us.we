package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.FoodAvailability
import com.ebay.hackathon.entity.Needy._
import com.ebay.hackathon.{DB, Logging}
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * Author sreejith on 09/11/14 4:40 AM.
 */

object FoodAvailabilityDAO extends BaseDAO[FoodAvailability, ObjectId](collection = DB.connection("foodAvailability"), entityClass = classOf[FoodAvailability])
with Logging {
  collection.ensureIndex(MongoDBObject(FoodAvailability.LOCATION -> "2d"), "locationIndex", unique = false)

  def addFoodAvailability(foodAvailablity: FoodAvailability) = {
    if (getFoodAvailability(foodAvailablity.userId) == Nil) {
      insert(foodAvailablity)
    }
  }

  def getFoodAvailability(userId: String) = {
    find(MongoDBObject(FoodAvailability.USER_ID -> userId, FoodAvailability.DATE -> DateTime.now())).toList
  }

  def getFoodAvailabilityForDate = {
    find(MongoDBObject(FoodAvailability.DATE -> DateTime.now())).toList
  }

  def updateStatus(userId: String)= {
    update(MongoDBObject(FoodAvailability.USER_ID -> userId), MongoDBObject("$set" -> MongoDBObject(APPROVAL_STATUS -> 1)))
  }


}
