package com.ebay.hackathon.endpoints

import com.ebay.hackathon.controllers.{AuthController, NeedyController, DeliveryController}
import org.scalatra.servlet.FileUploadSupport

/**
 * Author sreejith on 09/11/14 7:27 AM.
 */
class NeedyEndPoint extends ApiEndpoint with HttpSessionSupport with FileUploadSupport {

  private[this] def controller = {
    new NeedyController(signedInUser)
  }

  post("/create") {
    controller.createNeedy(
      param("name"),
      requiredParam("address"),
      intParam("totalPeople"),
      requiredDoubleParam("lat"),
      requiredDoubleParam("lng"))
  }

  get("/all"){
    controller.getNeedy
  }

  get("/basedOnAssignment/:volId"){
     controller.getNeedyBasedOnAssignment(requiredParam("volId"))
  }

  post("/assignVolunteer/:volId/needy/:needy"){
    controller.assignVolunteerToNeedy(requiredParam("volId"),requiredParam("needy"))
  }

}
