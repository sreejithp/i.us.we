package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.Needy
import com.ebay.hackathon.entity.Needy._
import com.ebay.hackathon.entity.traits.Identifiable
import com.ebay.hackathon.{DB, Logging}
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

/**
 * Author sreejith on 09/11/14 6:24 AM.
 */
object NeedyDAO extends BaseDAO[Needy, ObjectId](collection = DB.connection("needy"), entityClass = classOf[Needy])
with Logging {

  def createNeedy(needy: Needy): Unit = {
    insert(needy)
  }

  def getNeedy = {
    find(MongoDBObject.empty).toList
  }

  def getNeedyBasedOnAssignment(volId: String) = {
    find(MongoDBObject(VOLUNTEERS -> volId)).toList
  }

  def assignVolunteerToNeedy(volId: String, id: ObjectId) = {
    update(MongoDBObject(Identifiable.ID -> id), MongoDBObject("$set" -> MongoDBObject(VOLUNTEERS -> volId)))
  }

}
