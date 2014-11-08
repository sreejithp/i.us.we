package com.ebay.hackathon.entity

import com.ebay.hackathon.entity.traits.{Identifiable, SerialisableEntity}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.TypeImports._

/**
 * Created by sreejith on 08/11/14.
 */

class User extends UserCore[User]
with Identifiable[User]

class UserCore[T] extends SerialisableEntity[T] {
  var email: String = null
  var name: String = null
  var password: String = null
  var address: String = null
  var userType: Int = 0
  var pledge: Int = 0
  var pledgeDay: Int = 0
  var pledgeWeekDay: Int = 0
  var totalDonation: Int = 0

  import User._

  def asDBObject: DBObject = {
    val builder = MongoDBObject.newBuilder
    if (name != null) builder += NAME -> name
    if (email != null) builder += EMAIL -> email
    if (password != null) builder += PASSWORD -> password
    if (address != null) builder += ADDRESS -> address
    builder += USER_TYPE -> userType
    builder += PLEDGE -> pledge
    builder += PLEDGE_DAY -> pledgeDay
    builder += PLEDGE_WEEKDAY -> pledgeWeekDay
    builder += TOTAL_DONATION -> totalDonation
    builder.result()
  }

  def fromDBObject(dbObject: DBObject) = {
    name = getString(dbObject, NAME)
    email = getString(dbObject, EMAIL)
    address = getString(dbObject, ADDRESS)
    password = getString(dbObject, PASSWORD)
    userType = getInt(dbObject, USER_TYPE)
    pledge = getInt(dbObject, PLEDGE)
    pledgeDay = getInt(dbObject, PLEDGE_DAY)
    pledgeWeekDay = getInt(dbObject, PLEDGE_WEEKDAY)
    totalDonation = getInt(dbObject, TOTAL_DONATION)
    this.asInstanceOf[T]
  }

}

object User {
  val NAME: String = "name"
  val EMAIL: String = "email"
  val PASSWORD: String = "password"
  val ADDRESS: String = "address"
  val USER_TYPE: String = "userType"
  val PLEDGE: String = "pledge"
  val PLEDGE_DAY: String = "pledgeDay"
  val PLEDGE_WEEKDAY: String = "pledgeWeekDay"
  val TOTAL_DONATION: String = "totalDonation"


}