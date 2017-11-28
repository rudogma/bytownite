package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.{FixedLength, NotNullable}


object BooleanEncoder extends Encoder[Boolean] with FixedLength with NotNullable {

  override def blockLength: Int = 1

  val TRUE:Array[Byte] = Array[Byte](1)
  val FALSE:Array[Byte] = Array[Byte](0)

  final def encode(value: Boolean): Array[Byte] = if(value) TRUE else FALSE
}
