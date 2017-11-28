package org.rudogma.bytownite.decoders.arrays

import java.util

import org.rudogma.bytownite._
import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.utils.UnsyncByteArrayInputStream

import scala.reflect.ClassTag

class ArrayGenericDecoder[T: Decoder : ClassTag] extends Decoder[Array[T]] with HeaderBasedDecoder[Array[T]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = {

    val input = new UnsyncByteArrayInputStream(bytes)

    var value = new util.ArrayList[T]

    while(input.available() > 0){
      val extracted = implicitly[Decoder[T]].extract(input)

      value.add(extracted.value)
    }


    val array = new Array[T](value.size())

    value.toArray(array.asInstanceOf[Array[AnyRef]]).asInstanceOf[Array[T]]
  }
}
