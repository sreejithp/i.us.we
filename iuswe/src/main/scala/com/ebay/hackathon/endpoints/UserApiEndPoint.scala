package com.ebay.hackathon.endpoints

import com.ebay.hackathon.controllers.UserController

/**
 * Created by sreejith on 08/11/14.
 */

class UserApiEndPoint extends ApiEndpoint with HttpSessionSupport {

  private[this] def controller = {
    new UserController(signedInUser)
  }

  get("/id/:id") {
    controller.getUserById(requiredParam("id"))
  }

  get("/email/:email") {
    controller.getUserByEmail(requiredParam("email"))
  }

  post("/assignVolunteer/:volId/contributor/:contributorId"){
    controller.assignVolunteerToContributor(requiredParam("volId"),requiredParam("contributorId"))
  }

  get("/contributorsBasedOnVolunteer/:volId") {
    controller.getContributorsBasedOnVolunteer(requiredParam("volId"))
  }

  get("/contributors") {
    controller.getContributors
  }

  get("/volunteers") {
    controller.getVolunteers
  }

  get("/admins") {
    controller.getAdmins
  }


}
