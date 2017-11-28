package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object DoubleEncoder extends Encoder[Double] with FixedLength with NotNullable {

  final def blockLength: Int = 8

  final def encode(value: Double): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putDouble(bytes, 0, value)

    bytes
  }
}


