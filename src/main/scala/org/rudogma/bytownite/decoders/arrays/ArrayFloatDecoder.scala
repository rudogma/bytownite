package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.{Nullable}

object ArrayFloatDecoder extends Decoder[Array[Float]] with HeaderBasedDecoder[Array[Float]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {
    var value = new Array[Float](bytes.length / 4)
    var offset = 0

    var i = 0
    while(i < value.length){
      value(i) = Bits.getFloat(bytes, offset)
      offset += 4
      i += 1
    }

    value
  }
}
