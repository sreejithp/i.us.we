package com.ebay.hackathon.controllers

import java.io.Serializable
import java.net.{URLDecoder, URLEncoder}
import javax.servlet.http.HttpSession

import com.ebay.hackathon.endpoints.{StockResponses, Response, HttpSessionSupport}
import org.bson.types.ObjectId


object AuthController extends HttpSessionSupport {


  def directLogin(email: String, password: String)(implicit session: HttpSession): Response = {
    try {
      LOGGER.debug("Try user login " + email)
      StockResponses.OK // TODO;

    } catch {
      case e: Exception => LOGGER.error("Could not perform direct login ", e)
        Response(e)
    }
  }

  def register(email: String,
               password: String,
               name: String,
               subscriptionId: String)(implicit session: HttpSession): Response = {
    try {
//      setLoginSession(user)
      StockResponses.OK // TODO

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
