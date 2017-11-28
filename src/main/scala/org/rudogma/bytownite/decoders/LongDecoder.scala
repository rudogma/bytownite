package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite.decoders.CharDecoder.blockLength
import org.rudogma.bytownite._
import org.rudogma.bytownite.utils.Bits

object LongDecoder extends Decoder[Long] with FixedLength with NotNullable {

  def blockLength = 8

  def extract(input: InputStream) = {
    val bytes = new Array[Byte](blockLength)
    input.read(bytes)

    Extracted( Bits.getLong(bytes, 0), blockLength)
  }

}
