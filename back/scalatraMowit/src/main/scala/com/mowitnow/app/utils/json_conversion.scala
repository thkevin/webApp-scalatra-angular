package com.mowitnow.app.utils

import org.scalatra._
import com.mongodb.casbah.Imports._
import com.mowitnow.app.Objects.{Movement, Mower, Orientation, Position}
import org.json4s.JsonAST.{JArray, JField, JInt, JObject, JString}
import org.scalatra.json.JacksonJsonSupport
import org.json4s._
import org.json4s.mongo.{JObjectParser, ObjectIdSerializer}
import org.json4s.CustomSerializer
import org.json4s.JsonDSL._

class MowerSerializer extends CustomSerializer[Mower] ( implicit format =>
  ({
    case obj: JObject => {
      val str_to_do = (obj \ "to_do").extract[List[String]]
      val str_done = (obj \ "done").extract[List[String]]

      Mower(
        number = (obj \ "number").extract[Int],
        position = (obj \ "position").extract[Position],
        str_to_do.map(s => Movement.withName(s)),
        str_done.map(s => Movement.withName(s))
      )
    }
  },
    {
      case mower: Mower =>
        ("number" -> JInt(mower.number)) ~
          ("position" -> JObject(List(
            JField("x", JInt(mower.position.x)),
            JField("y", JInt(mower.position.y)),
            JField("o", JString(mower.position.o.toString))))) ~
          ("to_do" ->  JArray(mower.to_do.map(m => JString(m.toString)))) ~
          ("done" ->  JArray(mower.done.map(m => JString(m.toString))))
    })
)

class PositionSerializer extends CustomSerializer[Position] ( implicit format =>
  ({
    case obj: JObject => Position(x = (obj \ "x").extract[Int], y = (obj \ "y").extract[Int], o = Orientation.withName((obj \ "o").extract[String]))
  },
    {
      case position: Position =>
        ("x" -> JInt(position.x)) ~
          ("y" -> JInt(position.y)) ~
          ("o" -> JString(position.o.toString))
    })
)
trait Json4sMongoDbJsonConversion extends JacksonJsonSupport {

  implicit val jsonFormats = DefaultFormats + new ObjectIdSerializer + new PositionSerializer + new MowerSerializer

  def transformMongoObjectsToJson4s = {
    case dbo: DBObject => JObjectParser.serialize(dbo)
    case xs: TraversableOnce[_] =>
      JArray(xs.toList.map { x => JObjectParser.serialize(x) })
  }: RenderPipeline

  // hook into render pipeline
  override protected def renderPipeline = transformMongoObjectsToJson4s orElse super.renderPipeline
}