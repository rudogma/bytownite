package org.rudogma.bytownite.decoders

trait Implicits {

  implicit val DecoderBoolean:Decoder[Boolean] = BooleanDecoder

  implicit val DecoderByte:Decoder[Byte] = ByteDecoder
  implicit val DecoderShort:Decoder[Short] = ShortDecoder
  implicit val DecoderInt:Decoder[Int] = IntDecoder
  implicit val DecoderLong:Decoder[Long] = LongDecoder

  implicit val DecoderFloat:Decoder[Float] = FloatDecoder
  implicit val DecoderDouble:Decoder[Double] = DoubleDecoder

  implicit val DecoderString:Decoder[String] = StringDecoder.UTF8
}

object Implicits extends Implicits