package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object IntEncoder extends Encoder[Int] with FixedLength with NotNullable {

  final def blockLength: Int = 4

  final def encode(value: Int): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putInt(bytes, 0, value)

    bytes
  }
}
