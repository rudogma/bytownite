package org.rudogma.bytownite.utils

import java.nio.charset.Charset
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

object GZip {

  def compress(string: String, charset:Charset, bufferSize:Int = 512):Array[Byte] = {

    val out = new UnsyncByteArrayOutputStream()
    val gzos = new GZIPOutputStream(out, bufferSize)

    gzos.write(string.getBytes(charset))
    gzos.close()

    out.toByteArray
  }

  def decompress(bytes: Array[Byte], charset: Charset, bufferSize:Int = 1024): String = {
    val buffer = new Array[Byte](bufferSize)
    val gzis = new GZIPInputStream(new UnsyncByteArrayInputStream(bytes))

    val out = new UnsyncByteArrayOutputStream(bytes.length)


    var len = gzis.read(buffer)

    while (len > 0) {
      out.write(buffer, 0, len)
      len = gzis.read(buffer)
    }

    gzis.close()
    out.close()

    new String(out.toByteArray, charset)
  }
}
