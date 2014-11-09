package com.ebay.hackathon.controllers

import javax.servlet.http.HttpSession

import com.ebay.hackathon.dao.UserDAO
import com.ebay.hackathon.endpoints.{BaseController, Response}


class UserController(signedInUser: String) extends BaseController(signedInUser) {


  def getUserById(id: String)(implicit session: HttpSession): Response = requireSignIn {
    respond("getUser") {
      UserDAO.getUserById(id)
    }
  }

  def getUserByEmail(email: String)(implicit session: HttpSession): Response = requireSignIn {
    respond("getUserbyemail") {
      UserDAO.getUserByEmail(email)
    }
  }


}
