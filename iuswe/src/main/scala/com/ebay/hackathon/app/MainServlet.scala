package com.ebay.hackathon.app

import com.ebay.hackathon.Logging
import org.scalatra._
import scalate.ScalateSupport

class MainServlet extends IusweStack with Logging{

  get("/") {
    LOGGER.info("getting home page")
    redirect("/home")
  }

  get("/home") {
    app("/public/views/page-landing.html")
  }

  def app(path: String) = {
    val input = getServletContext().getResourceAsStream(path)
    contentType="text/html"
    scala.io.Source.fromInputStream(input).getLines().mkString("\n")
  }

}
