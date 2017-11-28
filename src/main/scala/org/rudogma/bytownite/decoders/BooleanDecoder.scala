package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite.{FixedLength, NotNullable}

object BooleanDecoder extends Decoder[Boolean] with FixedLength with NotNullable {
  def blockLength = 1

  def extract(input: InputStream) = Extracted(
    if( input.read() == 1 ) true else false,
    1
  )
}
