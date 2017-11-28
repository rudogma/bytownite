package org.rudogma.bytownite.decoders

import java.nio.charset.Charset

import org.rudogma.bytownite.Nullable

class StringDecoder(charset: Charset) extends Decoder[String] with HeaderBasedDecoder[String] with Nullable {
  override def decodeBody(bytes: Array[Byte]) = new String(bytes, charset)
}

object StringDecoder {
  implicit val UTF8:Decoder[String] = new StringDecoder(Charset.forName("UTF-8"))
}
