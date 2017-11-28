package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object CharEncoder extends Encoder[Char] with FixedLength with NotNullable {

  final def blockLength: Int = 2

  final def encode(value: Char): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putChar(bytes, 0, value)

    bytes
  }
}
