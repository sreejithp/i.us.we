import com.ebay.hackathon.app._
import com.ebay.hackathon.endpoints.{UserApiEndPoint, AuthApiEndPoint}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MainServlet, "/*")

    context.mount(new AuthApiEndPoint, "/api/auth")

    context.mount(new UserApiEndPoint, "/api/user")
  }
}
