val ScalatraVersion = "2.6.4"
organization := "com.mowitnow"
name := "Mow it now backend"
version := "0.1.0"
scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases
// offline := true

libraryDependencies ++= Seq(
  "org.mongodb"  %% "casbah" % "3.1.1",
  "org.json4s"   %% "json4s-mongo" % "3.5.2",
  "org.json4s"   %% "json4s-jackson" % "3.5.2",
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "joda-time" % "joda-time" % "2.9.2",
  "org.joda" % "joda-convert" % "1.8",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.14.v20181114" % "provided",
  "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided",
  "com.typesafe" % "config" % "1.3.3"
)

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

enablePlugins(ScalatraPlugin)
// enablePlugins(DockerPlugin)


// Docker settings
//mappings in (Compile, packageBin) ~= { _.filterNot(_._1.getName == "application.conf") }

//exportJars := true

// mainClass := Some("JettyLauncher")

// docker := (docker dependsOn sbt.Keys.`package`).value

// dockerfile in docker := {
//   val classpath = (fullClasspath in Runtime).value
//   val webappDir = (target in webappPrepare).value
//   val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
//   val classpathString = classpath.files.map("/app/lib/" + _.getName).mkString(":")

//   new Dockerfile {
//     from ("openjdk:latest")

//     runRaw("apt-get update")
//     runRaw("apt-get install -y vim curl wget unzip")

//     add(classpath.files, "/app/lib/")
//     add(webappDir, "/app/webapp")

//     runRaw("rm -rf /app/webapp/WEB-INF/lib")
//     runRaw("rm -rf /app/webapp/WEB-INF/classes")

//     volume("/app/conf")
//     volume("/app/data")

//     expose(80)
//     workDir("/app")

//     cmdRaw("java -Dconfig.file=$CONFIG_FILE -cp " + classpathString + "  " + mainclass)
//   }
// }