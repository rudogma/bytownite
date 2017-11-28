package org.rudogma.bytownite.encoders.arrays

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.Nullable
import org.rudogma.bytownite.encoders.HeaderBasedEncoder


object ArrayLongEncoder extends HeaderBasedEncoder[Array[Long]] with Nullable {

  override def encodeBody(value: Array[Long]) = {
    val body = new Array[Byte](value.length * 8)

    var i = 0
    var offset = 0
    while(i < value.length){
      Bits.putLong(body, offset, value(i))

      i += 1
      offset += 8
    }

    body
  }
}
