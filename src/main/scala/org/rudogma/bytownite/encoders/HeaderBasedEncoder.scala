package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.HeaderBased
import org.rudogma.bytownite.utils.Bits

trait HeaderBasedEncoder[@specialized T] extends Encoder[T] with HeaderBased {

  def headerLength:Int = 4

  def encodeBody(value:T):Array[Byte]

  override def encode(value: T) = {
    val body = encodeBody(value)

    val bytes = new Array[Byte](headerLength + body.length)

    Bits.putInt(bytes, 0, body.length)
    System.arraycopy(body, 0, bytes, headerLength, body.length)

    bytes
  }
}