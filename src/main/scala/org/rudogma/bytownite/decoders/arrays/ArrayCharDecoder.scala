package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayCharDecoder extends Decoder[Array[Char]] with HeaderBasedDecoder[Array[Char]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Char](bytes.length / 2)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getChar(bytes, offset)
      offset += 2
      i += 1
    }

    value
  }
}
