import com.mowitnow.app._
import org.scalatra._
import appconfig._
import javax.servlet.ServletContext
// MongoDb-specific imports
import com.mongodb.casbah.Imports._

class ScalatraBootstrap extends LifeCycle {
  private val conf = AppConfig.load
  private val mongoClient: MongoClient = MongoClient(conf.mongohost, conf.mongoport)

  override def init(context: ServletContext) {
    // Init the API with a connection to the mongo database
    val db = mongoClient(conf.mongodatabase)
    context.initParameters("org.scalatra.cors.allowCredentials") = "false"
    context.initParameters("org.scalatra.Port") = conf.port.toString
    context.initParameters("org.scalatra.environment") = AppEnvironment.asString(conf.env)
    context.mount(new MowItNowServlet(db), "/*")
  }

  // Close database connection when destroying the API
  override def destroy(context: ServletContext) {
    mongoClient.close
  }
}
