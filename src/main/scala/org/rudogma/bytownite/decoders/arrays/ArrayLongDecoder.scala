package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayLongDecoder extends Decoder[Array[Long]] with HeaderBasedDecoder[Array[Long]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Long](bytes.length / 8)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getLong(bytes, offset)
      offset += 8
      i += 1
    }

    value
  }
}
