package com.ebay.hackathon.app

import org.scalatra._
import scalate.ScalateSupport

class MainServlet extends IusweStack {

  get("/") {
    <html>
      <body>
        <h1>Welcome to I.Us.We</h1>
        Donate food to the needy
      </body>
    </html>
  }
  
}
