package com.mowitnow.app

import com.mowitnow.app.Objects.Movement._
import com.mowitnow.app.Objects.Orientation._
import com.mowitnow.app.Objects._

class Control {
  // Move a mower
  private def move(movement: Movement, mower: Mower, maxX: Int, maxY:Int): Mower = {
    println("movement : "+ movement.toString)
    movement match {
      case A => advance(mower, maxX, maxY)
      case _ => turn(movement, mower)
    }
  }

  // Advance a mower
  private def advance(mower: Mower, maxX: Int, maxY: Int): Mower ={
    val x = mower.position.x
    val y = mower.position.y

    val (newX, newY) = mower.position.o match {
      case N if y < maxY => (x, y + 1)
      case E if x < maxX => (x + 1, y)
      case W if x > 0 =>  (x -1, y)
      case S if y > 0 => (x, y - 1)
    }
    mower.copy(position = mower.position.copy(x = newX, y = newY))
  }

  // Turn a mower
  private def turn(movement: Movement, mower: Mower): Mower = {
    val o = mower.position.o
    val newO = movement match {
      case G => o match {
          case N => Orientation.W
          case E => Orientation.N
          case S => Orientation.E
          case W => Orientation.S
        }
      case D => o match {
          case N => Orientation.E
          case E => Orientation.S
          case S => Orientation.W
          case W => Orientation.N
        }
    }
    mower.copy(position = mower.position.copy(o = newO))
  }

  // Mow a cell of the surface
  private def mow(campaign: Campaign, mower: Mower): Unit ={
    campaign.surface(mower.position.x)(mower.position.y) = true
  }

  // Function that runs a mower in a surface
  private def run(mower: Mower, campaign: Campaign): Mower = {
    val actions = mower.to_do
    actions.foldLeft(mower) { (m, action) =>
      val u = m.copy(position = move(action, m, campaign.topX, campaign.topY).position, to_do = m.to_do.drop(1), done = action :: m.done)
      println(u)
      mow(campaign, u)
      u
    }
  }

  // Function that runs all mowers in the campaign
  def runAll(campaign: Campaign): Campaign = {
    campaign.copy(mowers = campaign.mowers.map(m => run(m, campaign)), surface = campaign.surface)
  }

  // Init a surface with false values
  def newArea(topX: Int, topY: Int): Array[Array[Boolean]] = {
    Array.ofDim[Boolean](topX + 1, topY + 1)
  }

  // Valid all mowers position in the campaign
  def validCampaign(campaign: Campaign)={
    campaign.mowers.forall(m => m.position.x <= campaign.topX && m.position.y <= campaign.topY)
  }

}

