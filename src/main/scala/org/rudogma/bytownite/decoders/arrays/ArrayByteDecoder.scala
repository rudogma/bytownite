package org.rudogma.bytownite.decoders.arrays

import java.io.InputStream

import org.rudogma.bytownite.decoders.{Decoder, HeaderBasedDecoder}
import org.rudogma.bytownite.{Nullable}

object ArrayByteDecoder extends Decoder[Array[Byte]] with HeaderBasedDecoder[Array[Byte]] with Nullable {

  override def decodeBody(bytes: Array[Byte]) = bytes
}
