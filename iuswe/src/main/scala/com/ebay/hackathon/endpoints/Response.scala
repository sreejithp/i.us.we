package com.ebay.hackathon.endpoints

case class Response(success: Boolean,
               result: Option[Any],
               errorCode: Option[String],
               errorMessage: Option[String],
               ts: Long = System.currentTimeMillis()) {
}

object Response {
  def apply(result: Any) = new Response(true, Some(result), None, None)

  def apply(e: Throwable) = new Response(false, None, None, Some(e.getMessage))
}

object StockResponses {
  val OK = Response("ok")
  val TRUE = Response("true")
  val FALSE = Response("false")

  val ERROR_NOT_FOUND = Response(new RuntimeException("not found"))
  val ERROR_UNAUTHORIZED = Response(new RuntimeException("unauthorised operation"))
  val ERROR_NOT_ALLOWED = Response(new RuntimeException("not allowed"))
  val ERROR_SIGNIN_REQUIRED = Response(new RuntimeException("you must sign in to do this"))

}
