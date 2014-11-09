package com.ebay.hackathon.controllers

import com.ebay.hackathon.dao.{FoodAvailabilityDAO, DeliveryInfoDAO}
import com.ebay.hackathon.dao.FoodAvailabilityDAO._
import com.ebay.hackathon.endpoints.BaseController
import com.ebay.hackathon.entity.FoodAvailability

/**
 * Author sreejith on 09/11/14 6:41 AM.
 */
class FoodAvailablityController(signedInUser: String) extends BaseController(signedInUser){

  def getFoodAvailability(userId: String) = {
    respond("getFoodAvailability") {
      FoodAvailabilityDAO.getFoodAvailability(userId)
    }
  }

  def getFoodAvailabilityForDate = {
    respond("getFoodAvailabilityForDate") {
      FoodAvailabilityDAO.getFoodAvailabilityForDate
    }
  }

  def updateStatus(userId: String) = {
    respond("getFoodAvailabilityForDate") {
      FoodAvailabilityDAO.updateStatus(userId)
    }
  }
}
