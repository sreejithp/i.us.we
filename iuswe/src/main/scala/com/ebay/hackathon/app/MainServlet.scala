package com.ebay.hackathon.app

import org.scalatra._
import scalate.ScalateSupport

class MainServlet extends IusweStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate"> to Scalate</a>.
      </body>
    </html>
  }
  
}
