package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object LongEncoder extends Encoder[Long] with FixedLength with NotNullable {

  final def blockLength: Int = 8

  final def encode(value: Long): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putLong(bytes, 0, value)

    bytes
  }
}

