package org.rudogma.bytownite.encoders.arrays

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.Nullable
import org.rudogma.bytownite.encoders.HeaderBasedEncoder


object ArrayCharEncoder extends HeaderBasedEncoder[Array[Char]] with Nullable {

  override def encodeBody(value: Array[Char]) = {
    val body = new Array[Byte](value.length * 2)

    var i = 0
    var offset = 0
    while(i < value.length){
      Bits.putChar(body, offset, value(i))

      i += 1
      offset += 2
    }

    body
  }
}
