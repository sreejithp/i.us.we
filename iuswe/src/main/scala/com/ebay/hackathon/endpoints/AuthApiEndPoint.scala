package com.ebay.hackathon.endpoints

import com.ebay.hackathon.controllers.AuthController

/**
 * Created by sreejith on 08/11/14.
 */

class AuthApiEndPoint extends ApiEndpoint {

  post("/register") {
    AuthController.register(requiredParam("email"),
                            requiredParam("password"),
                            requiredParam("name"),
                            requiredParam("address"),
                            requiredIntParam("userType"),
                            requiredIntParam("pledge"),
                            requiredIntParam("pledgeDay"),
                            requiredIntParam("pledgeWeekDay"))
  }

  post("/signin") {
    AuthController.directLogin(requiredParam("email"), requiredParam("password"))
  }

  post("signout") {
    AuthController.signOut
  }

}
