package com.ebay.hackathon.endpoints

import java.util.Date
import javax.servlet.http.HttpServletRequest

import com.ebay.hackathon.Logging
import org.json4s.Formats
import org.scalatra._
import org.scalatra.json._

abstract class BaseServlet extends ScalatraServlet with Logging {
  protected def param(key: String)(implicit request: HttpServletRequest) = {
    params.getOrElse(key, null)
  }

  protected def stringParam(key: String)(implicit request: HttpServletRequest) = {
    params.get(key) match {
      case Some(s) => if (s.trim.length == 0) None else Some(s)
      case None => None
    }
  }

  protected def wholeIntParamOption(key: String)(implicit request: HttpServletRequest): Option[Int] = {
    val i = intParam(key)
    if (i <= 0)
      None
    else
      Some(i)
  }

  protected def dateParam(key: String)(implicit request: HttpServletRequest): Option[Date] = {
    params.getAs[Date](key -> "MM/dd/yyyy")
  }

  protected def requiredDateParam(key: String)(implicit request: HttpServletRequest): Date = {
    params.getAs(key -> "MM/dd/yyyy") match {
      case Some(d) => d
      case None => throw new RuntimeException(s"Param $key not a valid date ${stringParam(key)}")
    }
  }

  protected def intParam(key: String)(implicit request: HttpServletRequest): Int = {
    val v = params.getOrElse(key, null)

    if (v == null)
      0
    else
      try {
        v.toInt
      }
      catch {
        case e: NumberFormatException =>
          LOGGER.debug("Param " + key + " passed but its not an int (" + v + ")")
          throw new RuntimeException("Param " + key + " passed but its not an int (" + v + ")")
      }
  }

  protected def csvListParam(key: String)(implicit request: HttpServletRequest) = {
    if (params.contains(key)) params(key).split(",").toList else Nil
  }

  protected def booleanParam(key: String)(implicit request: HttpServletRequest) = {
    try {
      params.get(key) match {
        case Some(value) =>
          value.toBoolean
        case None =>
          false
      }
    } catch {
      case e: Exception =>
        LOGGER.debug("Param " + key + " not passed as boolean (" + param(key) + ")")
        throw new RuntimeException("Param " + key + " not passed as boolean (" + param(key) + ")")
    }
  }

  protected def requiredParam(key: String)(implicit request: HttpServletRequest) = {
    if (!params.contains(key)) {
      LOGGER.debug("Required param " + key + " not passed")
      throw new RuntimeException("Required param " + key + " not passed")
    }

    params(key)
  }

  protected def requiredBooleanParam(key: String)(implicit request: HttpServletRequest) = {
    try {
      requiredParam(key).toBoolean
    } catch {
      case e: Exception =>
        LOGGER.debug("Required param " + key + " not passed as boolean (" + param(key) + ")")
        throw new RuntimeException("Required param " + key + " not passed as boolean (" + param(key) + ")")
    }
  }

  protected def requiredIntParam(key: String)(implicit request: HttpServletRequest) = {
    try {
      requiredParam(key).toInt
    } catch {
      case e: NumberFormatException =>
        LOGGER.debug("Required param " + key + " not passed as int (" + requiredParam(key) + ")")
        throw new RuntimeException("Required param " + key + " not passed as int (" + requiredParam(key) + ")")
    }
  }

  protected def requiredDoubleParam(key: String)(implicit request: HttpServletRequest) = {
    try {
      requiredParam(key).toDouble
    } catch {
      case e: NumberFormatException =>
        LOGGER.debug("Required param " + key + " not passed as double (" + requiredParam(key) + ")")
        throw new RuntimeException("Required param " + key + " not passed as double (" + requiredParam(key) + ")")
    }
  }

  protected def requiredFloatParam(key: String)(implicit request: HttpServletRequest) = {
    try {
      requiredParam(key).toFloat
    } catch {
      case e: NumberFormatException =>
        LOGGER.debug("Required param " + key + " not passed as float (" + requiredParam(key) + ")")
        throw new RuntimeException("Required param " + key + " not passed as float (" + requiredParam(key) + ")")
    }
  }

  protected def notfound = NotFound(quotedText("404 Not Found", "Requested content not found."))

  protected def forbidden = Forbidden(quotedText("403 Forbidden", "You do not have access to the requested content. Try logging in?"))

  private[this] def quotedText(code: String, title: String) = "<h1>" + code + "</h1><h3>" + title + "</h3><blockquote style='border-left: 10px solid #ccc; margin: 1.5em 10px; padding: 0.5em 10px; color: #999'></blockquote>"

}


trait ApiEndpoint extends BaseServlet with JacksonJsonSupport with CorsSupport {

  import org.json4s.native.Serialization.{write => swrite}

  protected implicit val jsonFormats: Formats = JsonFomats.jsonFormats

  before() {
    contentType = "application/json"
  }

  options("/*") {
    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"))
  }

  protected def skip(default: Int = 0)(implicit request: HttpServletRequest): Int = {
    params.getAsOrElse[Int]("skip", default)
  }

  protected def limit(default: Int = 10)(implicit request: HttpServletRequest): Int = {
    val l = params.getAsOrElse[Int]("limit", default)
    if (l == 0) default else l
  }

  protected def id: String = requiredParam("id")


}