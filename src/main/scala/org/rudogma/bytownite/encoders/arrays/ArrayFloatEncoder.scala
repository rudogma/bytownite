package org.rudogma.bytownite.encoders.arrays

import org.rudogma.bytownite.utils.Bits
import org.rudogma.bytownite.Nullable
import org.rudogma.bytownite.encoders.HeaderBasedEncoder


object ArrayFloatEncoder extends HeaderBasedEncoder[Array[Float]] with Nullable {

  override def encodeBody(value: Array[Float]) = {
    val body = new Array[Byte](value.length * 4)

    var i = 0
    var offset = 0
    while(i < value.length){
      Bits.putFloat(body, offset, value(i))

      i += 1
      offset += 4
    }

    body
  }
}
