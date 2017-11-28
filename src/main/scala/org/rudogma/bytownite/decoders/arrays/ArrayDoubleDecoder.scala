package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayDoubleDecoder extends Decoder[Array[Double]] with HeaderBasedDecoder[Array[Double]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Double](bytes.length / 8)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getDouble(bytes, offset)
      offset += 8
      i += 1
    }

    value
  }
}
