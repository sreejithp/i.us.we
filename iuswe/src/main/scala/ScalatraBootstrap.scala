import com.ebay.hackathon.app._
import com.ebay.hackathon.endpoints.{UserApiEndPoint, AuthApiEndPoint}
import com.ebay.hackathon.scheduler.Bootstrap
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  private[this] val appBootstrap = new Bootstrap()

  override def init(context: ServletContext) {

    context.mount(new MainServlet, "/*")

    context.mount(new AuthApiEndPoint, "/api/auth")

    context.mount(new UserApiEndPoint, "/api/user")

    appBootstrap.init()

  }

  override def destroy(context: ServletContext) = {
    appBootstrap.destroy()
    super.destroy(context)
  }
}
