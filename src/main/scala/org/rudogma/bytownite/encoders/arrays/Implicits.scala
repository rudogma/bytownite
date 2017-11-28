package org.rudogma.bytownite.encoders.arrays

import org.rudogma.bytownite.encoders.Encoder

trait Implicits {

  implicit val arrayCharEncoder:Encoder[Array[Char]] = ArrayCharEncoder

  implicit val arrayByteEncoder:Encoder[Array[Byte]] = ArrayByteEncoder
  implicit val arrayShortEncoder:Encoder[Array[Short]] = ArrayShortEncoder
  implicit val arrayIntEncoder:Encoder[Array[Int]] = ArrayIntEncoder
  implicit val arrayLongEncoder:Encoder[Array[Long]] = ArrayLongEncoder

  implicit val arrayFloatEncoder:Encoder[Array[Float]] = ArrayFloatEncoder
  implicit val arrayDoubleEncoder:Encoder[Array[Double]] = ArrayDoubleEncoder

  implicit def encoderArrayGeneric[T: Encoder]:Encoder[Array[T]] = new ArrayGenericEncoder[T]
}


object Implicits extends Implicits