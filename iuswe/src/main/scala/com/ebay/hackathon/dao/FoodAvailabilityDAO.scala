package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.FoodAvailability
import com.ebay.hackathon.{DB, Logging}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.gridfs.GridFS
import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * Author sreejith on 09/11/14 4:40 AM.
 */

object FoodAvailabilityDAO extends BaseDAO[FoodAvailability, ObjectId](collection = DB.connection("foodAvailability"), entityClass = classOf[FoodAvailability])
with Logging {
  collection.ensureIndex(MongoDBObject(FoodAvailability.LOCATION -> "2d"), "locationIndex", unique = false)

  def addFoodAvailability(foodAvailablity: FoodAvailability) = {
    if (getFoodAvailability(foodAvailablity.userId, foodAvailablity.date) == Nil) {
      insert(foodAvailablity)
    }
  }

  def getFoodAvailability(userId: String, date: DateTime) = {
    find(MongoDBObject(FoodAvailability.USER_ID -> userId, FoodAvailability.DATE -> DateTime.now())).toList
  }

}
