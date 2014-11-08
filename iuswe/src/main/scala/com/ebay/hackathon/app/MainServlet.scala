package com.ebay.hackathon.app

import com.ebay.hackathon.Logging
import org.scalatra._
import scalate.ScalateSupport

class MainServlet extends IusweStack with Logging{

  get("/") {
    LOGGER.info("getting home page")
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/app.ssp")
  }
  
}
