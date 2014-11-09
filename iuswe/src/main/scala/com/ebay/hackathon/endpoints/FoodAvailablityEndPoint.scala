package com.ebay.hackathon.endpoints

import com.ebay.hackathon.controllers.{FoodAvailablityController, DeliveryController}
import org.scalatra.servlet.FileUploadSupport

/**
 * Author sreejith on 09/11/14 9:36 AM.
 */
class FoodAvailablityEndPoint  extends ApiEndpoint with HttpSessionSupport with FileUploadSupport {

  private[this] def controller = {
    new FoodAvailablityController(signedInUser)
  }

  get("/:userId") {
    controller.getFoodAvailability(requiredParam("userId"))
  }


  get("/forToday") {
    controller.getFoodAvailabilityForDate
  }

  post("/statusUpdate/:userId") {
    controller.updateStatus(requiredParam("userId"))
  }

}
