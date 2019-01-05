package appconfig

import com.typesafe.config.ConfigFactory

sealed trait AppEnvironment
case object Development extends AppEnvironment
case object Staging extends AppEnvironment
case object Test extends AppEnvironment
case object Production extends AppEnvironment

object AppEnvironment {
  def fromString(s: String): AppEnvironment = {
    s match {
      case "development" => Development
      case "staging" => Staging
      case "test" => Test
      case "production" => Production
    }
  }
  def asString(s: AppEnvironment): String = {
    s match {
      case Development => "development"
      case Staging => "staging"
      case Test => "test"
      case Production => "production"
    }
  }
}

case class AppConfig( port: Int, host: String, webBase: String, assetsDirectory: String, env: AppEnvironment, mongohost: String, mongoport: Int, mongodatabase: String) {
  def isProduction = env == Production
  def isDevelopment = env == Development
}

object AppConfig {
  def load: AppConfig = {
    val cfg = ConfigFactory.load()

    val env = AppEnvironment.fromString(cfg.getString("environment"))
    val host = cfg.getString("host")
    val assetsDirectory = cfg.getString("assetsDirectory")
    val webBase = cfg.getString("webBase")
    val port = cfg.getInt("port")
    val mongohost = cfg.getString("mongohost")
    val mongoport = cfg.getInt("mongoport")
    val mongodatabase = cfg.getString("mongodatabase")

    AppConfig(port, host, webBase, assetsDirectory, env, mongohost, mongoport, mongodatabase)
  }
}
