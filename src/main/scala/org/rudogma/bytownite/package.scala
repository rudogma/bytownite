package org.rudogma

import java.io.EOFException

import org.rudogma.bytownite.decoders.Decoder
import org.rudogma.bytownite.encoders.Encoder

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros


package object bytownite {


  var BLOCK_LENGTH_LIMIT:Int = 30 * 1024 * 1024

  case class Debug() extends StaticAnnotation



  @inline def check_eof( bytesRead:Int, bytesExpected:Int){
    if( bytesRead == -1){
      throw new EOFException()
    }else if( bytesRead != bytesExpected ){
      throw new RuntimeException(s"Unexpected EOF. Read: $bytesRead, Expected: $bytesExpected")
    }
  }

  @inline def check_eof_strict( bytesRead:Int, bytesExpected:Int){
    if( bytesRead != bytesExpected ){
      throw new RuntimeException(s"Unexpected EOF. Read: $bytesRead, Expected: $bytesExpected")
    }
  }


  trait FixedLength {
    def blockLength:Int
  }

  trait HeaderBased {
    def headerLength:Int
  }

  trait Nullable {
    final val isNullable:Boolean = true
  }

  trait NotNullable {
    final val isNullable:Boolean = false
  }

  final def deriveEncoder[A]: Encoder[A] = macro DerivationMacros.materializeEncoder[A]
  final def deriveDecoder[A]: Decoder[A] = macro DerivationMacros.materializeDecoder[A]

  final def debug_deriveEncoder[A]: Encoder[A] = macro DerivationMacros.debug_materializeEncoder[A]
  final def debug_deriveDecoder[A]: Decoder[A] = macro DerivationMacros.debug_materializeDecoder[A]

}
