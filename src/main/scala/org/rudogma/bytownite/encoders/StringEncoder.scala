package org.rudogma.bytownite.encoders

import java.nio.charset.Charset

import org.rudogma.bytownite.Nullable

class StringEncoder(charset: Charset) extends HeaderBasedEncoder[String] with Nullable {
  override def encodeBody(value: String) = value.getBytes(charset)
}

object StringEncoder {
  implicit val UTF8:Encoder[String] = new StringEncoder(Charset.forName("UTF-8"))
}