package com.ebay.hackathon.dao

import com.ebay.hackathon.dao.NeedyDAO._
import com.ebay.hackathon.entity.User
import com.ebay.hackathon.entity.User.RATING
import com.ebay.hackathon.entity.User._
import com.ebay.hackathon.entity.traits.Identifiable
import com.ebay.hackathon.{DB, Logging}
import com.mongodb.WriteConcern
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject

import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * Author sreejith on 08/11/14 5:16 AM.
 */

object UserDAO extends BaseDAO[User, ObjectId](collection = DB.connection("user"), entityClass = classOf[User])
with Logging {

  def getUserByEmail(email: String) = {
    val response = find(MongoDBObject(User.EMAIL -> email)).toList
    val user = if (response != null && response.length > 0) response.head
    else null
    user
  }

  def getUserById(id: String, loadPrivate: Boolean = false) = {
    val response = find(MongoDBObject(Identifiable.ID -> new ObjectId(id))).toList
    val user = if (response != null && response.length > 0) {
      if (!loadPrivate) response.head.password = null
      response.head
    }
    else null
    user
  }

  def getUsersPledgedForToday = {
    val today = DateTime.now()
    val users = find(MongoDBObject(
      "$or" -> MongoDBList(
        MongoDBObject(PLEDGE -> 0),
        MongoDBObject(PLEDGE_DAY -> today.dayOfMonth().get()))),
      MongoDBObject(PLEDGE_WEEKDAY -> today.dayOfWeek().get())).toList
    users
  }

  def createUser(user: User) = {
    val userId = insert(user, WriteConcern.ACKNOWLEDGED).get
    LOGGER.debug("Created new user " + user.name + " / " + user.email)
    val registered_user = getUserById(userId.toString)
    registered_user
  }

  def incRating(id: String ) {
    update(MongoDBObject(Identifiable.ID -> new ObjectId(id)), MongoDBObject("$inc" -> MongoDBObject(RATING -> 1)))
  }

  def decRating(id: String ) {
    update(MongoDBObject(Identifiable.ID -> new ObjectId(id)), MongoDBObject("$inc" -> MongoDBObject(RATING -> -1)))
  }

  def assignVolunteerToContributor(volId: String, id: ObjectId) = {
    update(MongoDBObject(Identifiable.ID -> id), MongoDBObject("$set" -> MongoDBObject(VOLUNTEER_ID -> volId)))
  }

  def getContributorBasedOnVolunteers(volId: String) ={
    find(MongoDBObject(VOLUNTEER_ID -> volId)).toList
  }

  def getContributors = {
    find(MongoDBObject(USER_TYPE -> 0)).toList
  }

  def getVolunteers = {
    find(MongoDBObject(USER_TYPE -> 1)).toList
  }

  def getAdmins = {
    find(MongoDBObject(USER_TYPE -> 2)).toList
  }

}
