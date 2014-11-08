package com.ebay.hackathon.endpoints

import javax.servlet.http.{HttpServletRequest, HttpSession}

import com.ebay.hackathon.Logging
import com.ebay.hackathon.entity.User

import org.bson.types.ObjectId

class BaseControllerWrapper extends Logging with Respondable {

}

trait Respondable extends Logging {
  protected def wrapThrowable[T](name: String, identifier: Any = null, showException: Boolean = true)(f: => Response): Response = {
    try {
      f
    } catch {
      case e: Throwable =>
        if (showException)
          LOGGER.error("Could not " + name + (if (identifier != null) " for " + identifier.toString), e)
        else
          LOGGER.debug("Could not " + name + (if (identifier != null) " for " + identifier.toString) + ": " + e.getMessage)
        Response(e)
    }
  }

  protected def respond[T](name: String, identifier: Any = null, showException: Boolean = true)(f: => T): Response = {
    wrapThrowable(name, identifier, showException) {
      if (identifier != null)
        LOGGER.trace(name + " : " + identifier.toString)
        else
          LOGGER.trace(name)
      val result = f
      result match {
        case response: Response => response
        case nothing: Unit => StockResponses.OK
        case _ => Response(result)
      }
    }
  }
}

trait HttpSessionSupport extends Logging {
  protected val SESSION_KEY_USERID = "user.id"
  protected val SESSION_KEY_EMAIL = "user.email"

  protected def setLoginSession(user: User)(implicit session: HttpSession) = {
    session.setAttribute(SESSION_KEY_USERID, user.id)
    session.setAttribute(SESSION_KEY_EMAIL, user.email)
    Response(user)
  }

  def getSignedInUser(implicit session: HttpSession): Response = {
    if (session.getAttribute(SESSION_KEY_EMAIL) != null)
      Response(getSessionValue(SESSION_KEY_EMAIL))
    else
      StockResponses.ERROR_NOT_FOUND
  }

  def isSelf(username: String)(implicit session: HttpSession) = {
    (username != null && session.getAttribute(SESSION_KEY_USERID) != null) && (signedInEmail == username)
  }

  def isSignedIn(implicit session: HttpSession): Response = {
    if (session.getAttribute(SESSION_KEY_USERID) != null)
      StockResponses.TRUE
    else
      StockResponses.ERROR_NOT_FOUND
  }

  def signedInUser(implicit session: HttpSession) = getSessionValue(SESSION_KEY_USERID)

  def signedInEmail(implicit session: HttpSession) = getSessionValue(SESSION_KEY_EMAIL)

  def signedIn(implicit session: HttpSession) = getSessionValue(SESSION_KEY_USERID) != null && getSessionValue(SESSION_KEY_EMAIL) != null


  protected def getSessionValueAsOption(key: String)(implicit session: HttpSession) = {
    val attribute = session.getAttribute(key)
    if (attribute != null)
      Some(attribute.toString)
    else
      None
  }

  protected def getSessionValue(key: String)(implicit session: HttpSession) = {
    val attribute = session.getAttribute(key)
    if (attribute != null)
      attribute.toString
    else
      null
  }
}

class BaseController(signedInUser: String) extends Logging with Respondable {
  protected[this] implicit val signedInUserId = if (signedInUser == null) null else new ObjectId(signedInUser)

  protected def isSignedIn = signedInUser != null
  protected def requireSignIn(f: => Response): Response = {
    if(isSignedIn) f else StockResponses.ERROR_SIGNIN_REQUIRED
  }

  protected def requireSignIn(userId: ObjectId, f: => Response): Response = {
    if(isSignedIn && userId != null && signedInUserId == userId) f else StockResponses.ERROR_UNAUTHORIZED
  }

  protected def getSignedInUser(loadPrivate: Boolean = false) = ???

}
