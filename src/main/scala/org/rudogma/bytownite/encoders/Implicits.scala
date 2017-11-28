package org.rudogma.bytownite.encoders

trait Implicits {

  implicit val encoderBoolean:Encoder[Boolean] = BooleanEncoder

  implicit val encoderByte:Encoder[Byte] = ByteEncoder
  implicit val encoderShort:Encoder[Short] = ShortEncoder
  implicit val encoderInt:Encoder[Int] = IntEncoder
  implicit val encoderLong:Encoder[Long] = LongEncoder

  implicit val encoderFloat:Encoder[Float] = FloatEncoder
  implicit val encoderDouble:Encoder[Double] = DoubleEncoder

  implicit val encoderString:Encoder[String] = StringEncoder.UTF8

//  implicit val encoder_utf8string:Encoder[UTF8String] = null //??? //lifterF[Encoder].lift(StringEncoder.UTF8)
}

object Implicits extends Implicits