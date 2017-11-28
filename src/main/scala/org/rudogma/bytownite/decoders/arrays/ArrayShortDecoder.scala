package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayShortDecoder extends Decoder[Array[Short]] with HeaderBasedDecoder[Array[Short]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Short](bytes.length / 2)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getShort(bytes, offset)
      offset += 2
      i += 1
    }

    value
  }
}
