package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite
import org.rudogma.bytownite.HeaderBased
import org.rudogma.bytownite.utils.Bits


trait HeaderBasedDecoder[@specialized T] extends Decoder[T] with HeaderBased {

  def headerLength:Int = 4

  def decodeBody(bytes:Array[Byte]):T

  def extract(input: InputStream) = {
    val header = new Array[Byte](headerLength)
    input.read(header)

    val bodyLength = Bits.getInt(header, 0)



    if(bodyLength > bytownite.BLOCK_LENGTH_LIMIT){
      throw new RuntimeException(s"BodyLength overflow: ${bodyLength}")
    }

    val body = new Array[Byte](bodyLength)
    input.read(body)

    val value = decodeBody(body)

    Extracted(value, bodyLength + 4)
  }
}
