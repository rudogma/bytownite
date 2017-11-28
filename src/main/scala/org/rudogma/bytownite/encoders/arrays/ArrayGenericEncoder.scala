package org.rudogma.bytownite.encoders.arrays


import org.rudogma.bytownite.utils.UnsyncByteArrayOutputStream
import org.rudogma.bytownite.{Nullable}
import org.rudogma.bytownite.encoders.{Encoder, HeaderBasedEncoder}

class ArrayGenericEncoder[T: Encoder] extends HeaderBasedEncoder[Array[T]] with Nullable {

  override def encodeBody(value: Array[T]) = {
    val body = new UnsyncByteArrayOutputStream(512)

    var i = 0
    var offset = 0
    while(i < value.length){
      val bytes = implicitly[Encoder[T]].encode(value(i))
      //System.arraycopy(bytes, 0, body, offset, bytes.length)
      body.write(bytes)

      i += 1
      offset += bytes.length
    }

    body.toByteArray
  }
}
