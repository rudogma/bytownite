package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite.decoders.CharDecoder.blockLength
import org.rudogma.bytownite._
import org.rudogma.bytownite.utils.Bits

object ShortDecoder extends Decoder[Short] with FixedLength with NotNullable {

  def blockLength = 2

  def extract(input: InputStream) = {
    val bytes = new Array[Byte](blockLength)
    input.read(bytes)

    Extracted( Bits.getShort(bytes, 0), blockLength)
  }

}
