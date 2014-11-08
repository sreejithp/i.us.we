package com.ebay.hackathon.controllers

import javax.servlet.http.HttpSession

import com.ebay.hackathon.dao.UserDao
import com.ebay.hackathon.endpoints.{HttpSessionSupport, Response, StockResponses}
import com.ebay.hackathon.entity.User
import org.bson.types.ObjectId


object AuthController extends HttpSessionSupport {


  def directLogin(email: String, password: String)(implicit session: HttpSession): Response = {
    try {
      LOGGER.debug("Try user login " + email)
      val user = UserDao.getUserByEmail(email)

      if (user == null) {
        LOGGER.warn("Login attempted for a user which does not exist: " + email)
        StockResponses.ERROR_NOT_FOUND
      } else {

        if (user.password != null && user.password.equals(password)) {
          //TODO: MD5Hex
          setLoginSession(user)
        } else {
          StockResponses.ERROR_UNAUTHORIZED
        }
      }
      StockResponses.OK // TODO;

    } catch {
      case e: Exception => LOGGER.error("Could not perform direct login ", e)
        Response(e)
    }
  }

  def register(email: String,
               password: String,
               name: String,
               address: String,
               userType: Int,
               pledge: Int,
               pledgeDay: Int,
               pledgeWeekDay: Int)(implicit session: HttpSession): Response = {
    try {
      val user = new User()
      user.email = email
      user.name = name
      user.address = address
      user.userType = userType
      user.pledge = pledge
      user.pledgeDay = pledgeDay
      user.pledgeWeekDay = pledgeWeekDay
      user.password = password

      val registeredUser = UserDao.createUser(user)
      setLoginSession(registeredUser)
      Response(registeredUser)

    } catch {
      case e: Exception => LOGGER.error("Could not register ", e); Response(e)
    }
  }

  def signOut(implicit session: HttpSession): Response = {
    LOGGER.debug("signOut")
    session invalidate()
    StockResponses.OK
  }


}
