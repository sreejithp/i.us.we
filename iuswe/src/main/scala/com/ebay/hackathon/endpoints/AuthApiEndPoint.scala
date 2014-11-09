package com.ebay.hackathon.endpoints

import com.ebay.hackathon.controllers.AuthController

/**
 * Created by sreejith on 08/11/14.
 */

class AuthApiEndPoint extends ApiEndpoint {

  post("/register") {
    AuthController.register(requiredParam("email"),
      requiredParam("password"),
      param("name"),
      param("address"),
      intParam("userType"),
      intParam("pledge"),
      intParam("pledgeDay"),
      intParam("pledgeWeekDay"),
      requiredDoubleParam("lat"),
      requiredDoubleParam("lng"),
      requiredIntParam("totalCapacity")
    )
  }

  post("/signin") {
    AuthController.directLogin(requiredParam("email"), requiredParam("password"))
  }

  post("signout") {
    AuthController.signOut
  }

}
