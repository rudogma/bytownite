package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite._
import org.rudogma.bytownite.utils.Bits

object FloatDecoder extends Decoder[Float] with FixedLength with NotNullable {

  def blockLength = 4

  def extract(input: InputStream) = {
    val bytes = new Array[Byte](blockLength)
    input.read(bytes)

    Extracted( Bits.getFloat(bytes, 0), blockLength)
  }

}
