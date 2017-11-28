package org.rudogma.bytownite.encoders.arrays

import org.rudogma.bytownite.Nullable
import org.rudogma.bytownite.encoders.HeaderBasedEncoder

object ArrayByteEncoder extends HeaderBasedEncoder[Array[Byte]] with Nullable {

  override def encodeBody(value: Array[Byte]) = value
}