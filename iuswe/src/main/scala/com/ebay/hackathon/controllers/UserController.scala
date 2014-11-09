package com.ebay.hackathon.controllers

import javax.servlet.http.HttpSession

import com.ebay.hackathon.dao.UserDAO
import com.ebay.hackathon.endpoints.{BaseController, Response}
import org.bson.types.ObjectId


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

  def assignVolunteerToContributor(volId: String, id: String) = {
    respond("assignVolunteerToContributor") {
      UserDAO.assignVolunteerToContributor(volId, new ObjectId(id))
    }
  }

  def getContributorsBasedOnVolunteer(volId: String) = {
    respond("getContributorsBasedOnVolunteer") {
      UserDAO.getContributorsBasedOnVolunteer(volId)
    }
  }

  def getContributors = {
    respond("getContributors") {
      UserDAO.getContributors
    }
  }

  def getVolunteers = {
    respond("getVolunteers") {
      UserDAO.getVolunteers
    }
  }

  def getAdmins = {
    respond("getAdmins") {
      UserDAO.getAdmins
    }
  }


}
