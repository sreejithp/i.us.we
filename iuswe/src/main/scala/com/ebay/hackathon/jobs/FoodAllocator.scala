package com.ebay.hackathon.jobs

import com.ebay.hackathon.Logging
import com.ebay.hackathon.dao.{FoodAvailabilityDAO, UserDAO}
import com.ebay.hackathon.entity.FoodAvailability
import org.joda.time.DateTime


/**
 * Author sreejith on 09/11/14 3:04 AM.
 */
class FoodAllocator extends Runnable with Logging {

  def run() = {
    LOGGER.info("Starting to allocate tasks")
    val users = UserDAO.getUsersPledgedForToday
    for(user <- users){
      val foodAvailability = new FoodAvailability()
      foodAvailability.address = user.address
      foodAvailability.count = user.totalCapacity
      foodAvailability.date = DateTime.now()
      foodAvailability.loc = user.loc
      foodAvailability.status = 0 // NO by default
      foodAvailability.userId = user.id
      FoodAvailabilityDAO.addFoodAvailability(foodAvailability)
    }
    LOGGER.info("Task allocation done!")
  }

}
