package com.ebay.hackathon.dao

import com.ebay.hackathon.{Logging, DB}
import com.ebay.hackathon.entity.User
import com.ebay.hackathon.entity.traits.Identifiable
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

/**
 * Created by sreejith on 08/11/14.
 */

object UserDao extends BaseDAO[User, ObjectId](collection = DB.connection("user"), entityClass = classOf[User])
with Logging{

  def getUserByEmail(email: String) = {
    val response = find(MongoDBObject(User.EMAIL -> email)).toList
    val user = if (response != null && response.length > 0) response.head
    else null
    user
  }

  def getUserById(id: String, loadPrivate:Boolean = false) = {
    val response = find(MongoDBObject(Identifiable.ID -> new ObjectId(id))).toList
    val user = if (response != null && response.length > 0) {
      if(!loadPrivate) response.head.password = null
      response.head
    }
    else null
    user
  }

  def createUser(user: User) = {
    val userId = insert(user, WriteConcern.ACKNOWLEDGED).get
    LOGGER.debug("Created new user " + user.name + " / " + user.email)
    val registered_user = getUserById(userId.toString)
    registered_user
  }


}
