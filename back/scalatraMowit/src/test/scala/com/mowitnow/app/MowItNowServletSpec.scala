package com.mowitnow.app

import com.mongodb.casbah.Imports.MongoClient
import org.json4s.{DefaultFormats, Formats}
import com.mowitnow.app.Objects._
import com.mowitnow.app.utils.{MowerSerializer, PositionSerializer}
import org.json4s.jackson.Serialization.write
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.mongo.ObjectIdSerializer
import org.scalatra.test.specs2.ScalatraSpec

class MowItNowServletSpec extends ScalatraSpec  {def is = s2"""
   All GET / on  MowItNowServlet
       should be JSON formatted $jsonContentType_get
   All POST / on  MowItNowServlet
       should be JSON formatted $jsonContentType_post
   test POST /newcampaign on MowItNowServlet
        should be equal too the expected output $expected
  """

  val db = MongoClient()("mowitnow_db")
  val controller = new Control
  implicit lazy val jsonFormats: Formats = DefaultFormats + new ObjectIdSerializer + new PositionSerializer + new MowerSerializer

  addServlet(new MowItNowServlet(db), "/*")

  // ***************************************************************************************************************************************************************************************
  //    Mowers
  val mow1 = Mower(1, Position(1,2,Orientation.N), List(Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.A), List())
  val mow2 = Mower(2, Position(3,3,Orientation.E), List(Movement.A, Movement.A, Movement.D, Movement.A, Movement.A, Movement.D, Movement.A, Movement.D, Movement.D, Movement.A), List())
  //    Campaign 5x5 with mowers mow1 & mow2
  val input = Campaign("id","Nom", 5, 5, null, mow1::mow2::Nil, controller.newArea(5,5))

  //    Input to json
  val bdy = write(input)

  /*
  For mower 1, initial position was 1, 2, N, final position must be 1, 3, N
  For mower 2, initial position was 3, 3, E, final position must be 5, 1, E
  For both mowers, to_do is the list of movements instructions for the mower

  Datetime is set to null because is changes each millisecond so can't be set a value to expect
  Surface is the matrix representing the area to mow (It's for the Web UI)
   */
  val output =
    """
      |{
      | "id":"id",
      |	"name": "Nom",
      |	"topX": 5,
      |	"topY": 5,
      |	"datetime": null,
      |	"mowers": [{
      |		"number": 1,
      |		"position": {
      |			"x": 1,
      |			"y": 3,
      |			"o": "N"
      |		},
      |		"to_do": [],
      |		"done": ["A", "A", "G", "A", "G", "A", "G", "A", "G"]
      |	}, {
      |		"number": 2,
      |		"position": {
      |			"x": 5,
      |			"y": 1,
      |			"o": "E"
      |		},
      |		"to_do": [],
      |		"done": ["A", "D", "D", "A", "D", "A", "A", "D", "A", "A"]
      |	}],
      |	"surface": [
      |		[false, true, true, false, false, false],
      |		[false, true, true, true, false, false],
      |		[false, false, false, false, false, false],
      |		[false, false, false, false, false, false],
      |		[false, true, false, true, false, false],
      |		[false, true, true, true, false, false]
      |	]
      |}
      |
    """.stripMargin
  
  def expected = post("/campaign/run", bdy) {
    parse(body) must_== parse(output)
  }

  def jsonContentType_get = get("/countcampaigns") {
    header("Content-Type") must contain ("application/json;charset=utf-8")
  }

  def jsonContentType_post = post("/campaign/run", bdy) {
    header("Content-Type") must contain ("application/json;charset=utf-8")
  }
}
