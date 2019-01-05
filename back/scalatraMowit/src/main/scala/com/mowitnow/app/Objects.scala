package com.mowitnow.app

import java.util.UUID

object Objects {
  object Orientation extends Enumeration {
    type Orientation = Value
    val N = Value("N")
    val E = Value("E")
    val S = Value("S")
    val W = Value("W")
  }

  object Movement extends Enumeration {
    type Movement = Value
    val A = Value("A")
    val D = Value("D")
    val G = Value("G")
  }

  case class MdbId(id: String = UUID.randomUUID().toString) {
    override def toString: String = id
  }

  case class Campaign (id: String, name: String, topX: Int, topY: Int, datetime: String, mowers: List[Mower], surface: Array[Array[Boolean]])

  case class Position(x: Int, y: Int, o: Orientation.Value)

  case class Mower (number: Int, position: Position, to_do: List[Movement.Value], done : List[Movement.Value])
}
