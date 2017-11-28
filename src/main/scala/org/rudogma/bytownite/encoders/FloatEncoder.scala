package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{FixedLength, NotNullable}

object FloatEncoder extends Encoder[Float] with FixedLength with NotNullable {

  final def blockLength: Int = 4

  final def encode(value: Float): Array[Byte] = {
    val bytes = new Array[Byte](blockLength)

    Bits.putFloat(bytes, 0, value)

    bytes
  }
}
