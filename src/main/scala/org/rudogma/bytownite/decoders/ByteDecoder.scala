package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite.decoders.LongDecoder.blockLength
import org.rudogma.bytownite.{FixedLength, NotNullable}

object ByteDecoder extends Decoder[Byte] with FixedLength with NotNullable {
  def blockLength = 1

  def extract(input: InputStream) = {
    val bytes = new Array[Byte](blockLength)
    input.read(bytes)

    Extracted( bytes(0), blockLength)
  }
}
