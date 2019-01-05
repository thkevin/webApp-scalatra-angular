package com.mowitnow.app

import org.joda.time._
import com.mongodb.casbah.Imports._
import com.mowitnow.app.Objects._
import com.mowitnow.app.utils.Json4sMongoDbJsonConversion
import org.scalatra._

class MowItNowServlet(db: MongoDB) extends ScalatraServlet with ApiFormats with Json4sMongoDbJsonConversion with CorsSupport with ScalatraBase {
  // Database management
  val storage = new Storage(db)

  // Controller
  val controller = new Control

  // Default API output formats : JSON
  before() {
    contentType = formats("json")
  }

  // Create a new campaign and insert in the database without running
  post("/campaign/create"){
    val postCampaign = parsedBody.extract[Campaign]
    val loc = new LocalDate().toString()
    val locTime = new LocalTime().toString("HH:ss:SS")
    val c = Campaign(postCampaign.id, postCampaign.name, postCampaign.topX, postCampaign.topY, loc+" "+locTime, postCampaign.mowers, controller.newArea(postCampaign.topX, postCampaign.topY))
    storage.insertCampaign(c)
  }

  // Create a new campaign, run it  and insert it
  post("/campaign/create_run"){
    val postCampaign = parsedBody.extract[Campaign]
    val c = Campaign(postCampaign.id, postCampaign.name, postCampaign.topX, postCampaign.topY, "", postCampaign.mowers, controller.newArea(postCampaign.topX, postCampaign.topY))
    val runned = controller.runAll(c)
    storage.insertCampaign(runned)
    runned
  }

  // Create a new campaign, run it  and insert it
  post("/campaign/run"){
    val postCampaign = parsedBody.extract[Campaign]
    val result = controller.runAll(postCampaign)
    storage.insertCampaign(result)
    result
  }

  // Update a Campaign
  post("/campaign/update"){
    val postCampaign = parsedBody.extract[Campaign]
    val result = controller.runAll(postCampaign)
    storage.updateCampaign(result)
    result
  }

  // Get a campaign
  get("/getcampaign/:id"){
    val id = params("id")
    storage.getCampaign(id)
  }

  // Find all campains containing the name in the "name" field
  get("/getcampaigns/name/:name"){
    val name = params("name")
    storage.getCampaignByName(name)
  }

  // Get all campaigns
  get("/getcampaigns"){
    storage.getCampaigns()
  }

  // get the number of campaigns in the database
  get("/countcampaigns"){
    "count" -> storage.campaignCount
  }

  // get the number of mowers in the database
  get("/countmowers"){
    "count" -> storage.mowersCount()
  }

  // Tests
  get("/testmove"){
    /*
    val mowy = Mower(1, Position(1,2,Orientation.N), List(Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.A), List())
    val mowa = Mower(2, Position(3,3,Orientation.E), List(Movement.A, Movement.A, Movement.D, Movement.A, Movement.A, Movement.D, Movement.A, Movement.D, Movement.D, Movement.A), List())
    val camp = controller.runAll( Campaign("Nom", 5, 5, null, mowy::mowa::Nil, controller.newArea(5,5)) )
    */
    storage.dropthem()
  }

}
