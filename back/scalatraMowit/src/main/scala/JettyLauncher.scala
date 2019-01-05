// import org.eclipse.jetty.server.Server
// import org.eclipse.jetty.servlet.DefaultServlet
// import org.eclipse.jetty.webapp.WebAppContext
// import org.scalatra.servlet.ScalatraListener
// import appconfig._

// object JettyLauncher {
//   def main(args: Array[String]) {
//     val conf = AppConfig.load

//     val server = new Server(conf.port)
//     val context = new WebAppContext()
//     context setContextPath "/"
//     context.setResourceBase("src/main/webapp")
//     context.addEventListener(new ScalatraListener)
//     context.addServlet(classOf[DefaultServlet], "/")
//     context.setInitParameter("org.scalatra.cors.allowCredentials", "false")
//     server.setHandler(context)
//     server.start
//     server.join
//   }
// }