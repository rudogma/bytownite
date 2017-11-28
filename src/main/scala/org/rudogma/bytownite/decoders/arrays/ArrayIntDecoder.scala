package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayIntDecoder extends Decoder[Array[Int]] with HeaderBasedDecoder[Array[Int]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Int](bytes.length / 4)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getInt(bytes, offset)
      offset += 4
      i += 1
    }

    value
  }
}
