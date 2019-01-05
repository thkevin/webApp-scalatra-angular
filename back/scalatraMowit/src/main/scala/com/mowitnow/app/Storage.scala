package com.mowitnow.app

import java.util.Date

import com.mongodb.BasicDBObject
import com.mongodb.casbah.Imports._
import com.mowitnow.app.Objects._
import com.mowitnow.app.utils.{MowerSerializer, PositionSerializer}
import org.json4s.DefaultFormats
import org.json4s.mongo.ObjectIdSerializer
import org.scalatra.FutureSupport

import scala.concurrent.{ExecutionContext, Future}


class Storage(db: MongoDB){
  implicit val jsonFormats = DefaultFormats + new ObjectIdSerializer + new PositionSerializer + new MowerSerializer

  val campaignsColl = db("campaigns")
  val mowersColl = db("mowers")

  def campaignCount: Int = campaignsColl.count()

  def campaignsFind() = campaignsColl.find

  def mowersFind() = mowersColl.find

  def mowersCount() = mowersColl.count()

  // Insert a Campain object in the database
  def insertCampaign(campaign: Campaign) ={
    val cId = MdbId()
    val campaignMongoObject = MongoDBObject("_id" -> cId.toString, "name" -> campaign.name, "topX"-> campaign.topX, "topY"-> campaign.topY, "datetime"-> campaign.datetime,  "surface"-> campaign.surface.toList)
    campaign.mowers.foreach(m => insertMower(m, cId.toString))
    campaignsColl.insert(campaignMongoObject)
  }

  // Update a campaign by removing old mowers with the campaign id, and inserting the new ones and setting new the surface value
  def updateCampaign(campaign: Campaign) ={
    removeCampMowers(campaign.id)
    val query = MongoDBObject("_id" -> campaign.id)
    val update = $set("surface"-> campaign.surface.toList)
    val result = campaignsColl.update( query, update )
    campaign.mowers.foreach(m => insertMower(m, campaign.id))
    println("Updating Campaign")
    result.getN
  }

  // Get the campaign by id
  def getCampaign(id:String) ={
    val toFind = MongoDBObject( "_id" -> id)
    campaignsColl.findOne(toFind) match {
      case Some(result) => convertObjectToCampaign(result)
      case _ => None
    }
  }

  // Get the campaign by name
  def getCampaignByName(name:String) ={
    val reg = ".*"+name+".*"
    val toFind = MongoDBObject( "name" ->  reg.r)
    val fields = MongoDBObject("_id" -> 1, "datetime" -> 1, "name" -> 1, "topX"-> 1, "topY" -> 1)
    campaignsColl.find(toFind, fields)
  }

  // Get the campaigns
  def getCampaigns() ={
    val fields = MongoDBObject("_id" -> 1, "datetime" -> 1, "name" -> 1, "topX"-> 1, "topY" -> 1)
    campaignsColl.find(MongoDBObject(), fields).sort(MongoDBObject("datetime" -> -1))
  }

  // Converts Mongo DB object to Campaign object
  private def convertObjectToCampaign(obj: MongoDBObject): Campaign ={
    val id = obj.getAs[String]("_id").get
    val name = obj.getAs[String]("name").get
    val topX = obj.getAs[Int]("topX").get
    val topY = obj.getAs[Int]("topY").get
    val datetime = obj.getAs[String]("datetime").get
    val surfaceList = obj.getAs[MongoDBList]("surface").get
    val surface = for (e <- surfaceList.toArray)
      yield for (x <- e.asInstanceOf[BasicDBList].toArray) yield x.asInstanceOf[Boolean]

    val mowers = getMowers(id)
    Campaign(id, name, topX, topY, datetime, mowers, surface)
  }

  private def removeCampMowers(cId: String) ={
    val toRemove = MongoDBObject("campId" -> cId)
    mowersColl.findAndRemove(toRemove)
  }

  // Insert a mower in the mowers collection
  private def insertMower(mower: Mower, cId: String) ={
    val mowerMongoObject = MongoDBObject(
      "campId" -> cId,
      "number" -> mower.number,
      "position" -> MongoDBObject("x" -> mower.position.x, "y" -> mower.position.y, "o" -> mower.position.o.toString),
      "to_do" -> mower.to_do.map(m => m.toString),
      "done" -> mower.done.map(m => m.toString)
    )
    mowersColl.insert(mowerMongoObject)
  }

  // Gets all mowers in a campaign by their ID
  def getMowers(cId: String) ={
    val toFind = MongoDBObject("campId" -> cId)
    val mowers = mowersColl.find(toFind)
    mowers.map(dbObj => convertDbObjectToMower(dbObj)).toList
  }

  // Convert a Mongo object into a Mower
  private def convertDbObjectToMower(obj: MongoDBObject): Mower = {
      val number = obj.getAs[Int]("number").get
      val position = Position( obj.getAs[Int]("position.x").get, obj.getAs[Int]("position.y").get, Orientation.withName(obj.getAs[String]("position.o").get) )
      val to_do = obj.getAs[List[String]]("to_do").get.map(s => Movement.withName(s))
      val done = obj.getAs[List[String]]("done").get.map(s => Movement.withName(s))

      Mower(number, position, to_do, done)
  }

  def dropthem()={
    campaignsColl.drop()
    mowersColl.drop()
  }

}