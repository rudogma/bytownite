package org.rudogma.bytownite.decoders.arrays

import org.rudogma.bytownite.decoders.Decoder

import scala.reflect.ClassTag

trait Implicits {

  implicit val arrayCharDecoder:Decoder[Array[Char]] = ArrayCharDecoder

  implicit val arrayByteDecoder:Decoder[Array[Byte]] = ArrayByteDecoder
  implicit val arrayShortDecoder:Decoder[Array[Short]] = ArrayShortDecoder
  implicit val arrayIntDecoder:Decoder[Array[Int]] = ArrayIntDecoder
  implicit val arrayLongDecoder:Decoder[Array[Long]] = ArrayLongDecoder

  implicit val arrayFloatDecoder:Decoder[Array[Float]] = ArrayFloatDecoder
  implicit val arrayDoubleDecoder:Decoder[Array[Double]] = ArrayDoubleDecoder

  implicit def arrayGenericDecoder[T: Decoder: ClassTag]:Decoder[Array[T]] = new ArrayGenericDecoder[T]
}

object Implicits extends Implicits