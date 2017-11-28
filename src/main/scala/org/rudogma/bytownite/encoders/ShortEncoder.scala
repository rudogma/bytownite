package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object ShortEncoder extends Encoder[Short] with FixedLength with NotNullable {

  final def blockLength: Int = 2

  final def encode(value: Short): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putShort(bytes, 0, value)

    bytes
  }
}