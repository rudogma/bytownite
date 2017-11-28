package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.{FixedLength, NotNullable}

object ByteEncoder extends Encoder[Byte] with FixedLength with NotNullable {

  override def blockLength: Int = 1

//  val ONE:Array[Byte] = Array[Byte](1)
//  val ZERO:Array[Byte] = Array[Byte](0)

  override def encode(value: Byte): Array[Byte] = Array(value)
}
