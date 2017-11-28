package org.rudogma.bytownite

import org.rudogma.bytownite.decoders.Decoder
import org.rudogma.bytownite.encoders.Encoder

import scala.reflect.macros.blackbox

class DerivationMacros(val c: blackbox.Context) {

  import c.universe._



  def materializeEncoder[T: c.WeakTypeTag]: c.Expr[Encoder[T]] = materializeEncoderImpl[T]()
  def materializeDecoder[T: c.WeakTypeTag]: c.Expr[Decoder[T]] = materializeDecoderImpl[T]()

  def debug_materializeEncoder[T: c.WeakTypeTag]: c.Expr[Encoder[T]] = materializeEncoderImpl[T](true)
  def debug_materializeDecoder[T: c.WeakTypeTag]: c.Expr[Decoder[T]] = materializeDecoderImpl[T](true)


  private[this] def fail(tpe: Type): Nothing = c.abort(
    c.enclosingPosition,
    s"Could not identify primary constructor for $tpe"
  )

  private[this] def materializeEncoderImpl[T: c.WeakTypeTag](debug:Boolean = false): c.Expr[Encoder[T]] = {


    val tpe = weakTypeOf[T]

    val repr = productRepr(tpe)

    productRepr(tpe).fold(fail(tpe)){ repr =>

      val encoders = repr.members.map{ m =>
        q"val ${m.encoderName}:Encoder[${m.tpe}] = implicitly[Encoder[${m.tpe}]]"
      }

      val encodersList = q"protected val encodersList:Array[Encoder[_]] = Array(..${repr.members.map( _.encoderName)})"


      val doEncode =
        q"""
           @inline def doEncode[T](value:T, encoder:Encoder[T]):Unit = {
              val b = encoder.encode(value)
              System.arraycopy(b, 0, bytes, offset, b.length)
              offset += b.length
           }
         """

      val flist = repr.members.map{ m =>
        q"""doEncode(value.${m.name}, ${m.encoderName})"""
      }

      val fixedLengthEncoder =
        q"""
           val blockLengthCalculated = encodersList.map(_.asInstanceOf[FixedLength].blockLength).sum

           new Encoder[$tpe] with FixedLength with Nullable {
              def blockLength = blockLengthCalculated


              final def encode(value:${tpe}):Array[Byte] = {
              val bytes = new Array[Byte](blockLength)
              var offset = 0

              $doEncode

              ..$flist

                bytes
              }
           }
         """


      val doEncodeNullable =
        q"""
           @inline def doEncode[T](value:T, encoder:Encoder[T]):Unit = {
              if(encoder.isNullable){
                         if(value == null){
                           nullBits.set(nullBitsIndex)
                         }else{
                           val b = encoder.encode(value)
                           output.write(b)
                           offset += b.length
                         }
                         nullBitsIndex += 1
           
             }else{
                         val b = encoder.encode(value)
                         output.write(b)
                         offset += b.length
                       }


           }
         """
      
      val varLengthEncoder =
        q"""

           val nullBitsCount = encodersList.count(_.isNullable)
           val nullBitsBytes = if( nullBitsCount % 8 > 0){
                       (nullBitsCount / 8) + 1
                   }else {
              (nullBitsCount / 8)
           }

            new Encoder[$tpe] with HeaderBasedEncoder[$tpe] with Nullable {

               final def encodeBody(value:${tpe}):Array[Byte] = {
                   val output = new UnsyncByteArrayOutputStream(100)
                   var offset = 0
                   val nullBits = new BitSet(nullBitsCount)
                   var nullBitsIndex = 0

                    output.pos += nullBitsBytes

                  $doEncodeNullable
           
                  ..$flist

           val bools = BitSetUtils.toArrayBoolean(nullBits).toList

                  val bytes = output.toByteArray
                  System.arraycopy(BitSet.toByteArrayTrimmed(nullBits), 0, bytes, 0, nullBitsBytes)
                  bytes
                         }
                      }
         """


      val body =
        q"""

          import org.rudogma.bytownite.utils._

          ..$encoders
          ..$encodersList

          val isFixedLength:Boolean = encodersList.forall(_.isInstanceOf[FixedLength])

          if(isFixedLength){
            $fixedLengthEncoder
          }else{
            $varLengthEncoder
          }
        """

      if(debug){
        println(s"--- Derived Encoder[${tpe}] body ---")
        println(body)
      }

      c.Expr[Encoder[T]](body)
    }

  }




  private[this] def materializeDecoderImpl[T: c.WeakTypeTag](debug:Boolean = false): c.Expr[Decoder[T]] = {

    val tpe = weakTypeOf[T]



    val repr = productRepr(tpe)

    productRepr(tpe).fold(fail(tpe)){ repr =>


      val decoders = repr.members.map{ m =>
        q"val ${m.decoderName}:Decoder[${m.tpe}] = implicitly[Decoder[${m.tpe}]]"
      }

      val decodersList = q"protected val decodersList:Array[Decoder[_]] = Array(..${repr.members.map( _.decoderName)})"


      val doDecode =
        q"""
           @inline def doDecode[T](decoder:Decoder[T]):T = {
              val r = decoder.extract(input)

              bytesRead += r.bytes
              r.value
           }
         """

      val flist = repr.members.map{ m =>
        q"""doDecode(${m.decoderName})"""
      }


      val tpeName = tpe.typeConstructor.typeSymbol.name.toTermName

      val fixedLengthDecoder =
        q"""
           val blockLengthCalculated = decodersList.map(_.asInstanceOf[FixedLength].blockLength).sum

           new Decoder[$tpe] with FixedLength with Nullable {
              def blockLength = blockLengthCalculated


              final def extract(input:InputStream) = {
                var bytesRead = 0

               $doDecode

                Extracted( ${tpeName}(..$flist), bytesRead)
              }
           }
         """

      val doDecodeNullable =
        q"""
           @inline def doDecode[T](decoder:Decoder[T]):T = {

              if(decoder.isNullable){
                 if(nullBits.fastGet(nullBitsIndex)){
                     nullBitsIndex += 1
                     null.asInstanceOf[T]
                 }else{
                    nullBitsIndex += 1

                    val r = decoder.extract(input)
                      
                    r.value
                 }
              }else{
                val r = decoder.extract(input)
           
                r.value
              }
           }
         """

      val varLengthDecoder =
        q"""
           val nullBitsCount = decodersList.count(_.isNullable)
           val nullBitsBytes = if( nullBitsCount % 8 > 0){
                                  (nullBitsCount / 8) + 1
                              }else {
                         (nullBitsCount / 8)
                      }

           new Decoder[$tpe] with HeaderBasedDecoder[$tpe] with Nullable {

            final def decodeBody(bytes:Array[Byte]):$tpe = {
            val input = new UnsyncByteArrayInputStream(bytes)
            val nullBits = new BitSet(nullBitsCount)
            var nullBitsIndex = 0

              $doDecodeNullable

              nullBits.replace(bytes, nullBitsCount, 0)
              input.skip(nullBitsBytes)

              val bools = BitSetUtils.toArrayBoolean(nullBits).toList

              ${tpeName}(..$flist)
            }
           }
         """

      val body = q"""
                    import java.io.InputStream
                    import org.rudogma.bytownite.utils._


             ..$decoders
             ..$decodersList
            val isFixedLength:Boolean = decodersList.forall(_.isInstanceOf[FixedLength])

                    if(isFixedLength){
                       $fixedLengthDecoder
                    }else{
                       $varLengthDecoder
                    }

        """

      if(debug){
        println(s"--- Derived Decoder[${tpe}] body ---")
        println(body)
      }

      c.Expr[Decoder[T]](body)
    }

  }





  private[this] val encoderSymbol: Symbol = c.symbolOf[Encoder[_]]
//  private[this] val decoderSymbol: Symbol = c.symbolOf[Decoder]
  private[this] val encoderTC: Type = typeOf[Encoder[_]].typeConstructor
//  private[this] val decoderTC: Type = typeOf[Decoder[_]].typeConstructor




  private[this] case class Instance(tc: Type, tpe: Type, name: TermName) {
    def resolve(): Tree = c.inferImplicitValue(appliedType(tc, List(tpe))) match {
      case EmptyTree => c.abort(c.enclosingPosition, s"Could not find implicit $tpe")
      case instance => instance
    }
  }

  private[this] case class Instances(encoder: Instance, decoder: Instance)
  private[this] case class Member(name: TermName, decodedName: String, tpe: Type, encoderName:TermName, decoderName:TermName)

  private[this] case class ProductRepr(members: List[Member]) {
    val instances: List[Instances] =
      members.foldLeft(List.empty[Instances]) {
        case (acc, Member(_, _, tpe, _, _)) if acc.find(_.encoder.tpe =:= tpe).isEmpty =>
          val instances = Instances(
            Instance(encoderTC, tpe, TermName(c.freshName("encoder"))),
            null
//            Instance(decoderTC, tpe, TermName(c.freshName("decoder")))
          )

          instances :: acc
        case (acc, _) => acc
      }.reverse

    private[this] def fail(tpe: Type): Nothing = c.abort(c.enclosingPosition, s"Invalid instance lookup for $tpe")

    def encoder(tpe: Type): Instance = instances.map(_.encoder).find(_.tpe =:= tpe).getOrElse(fail(tpe))
    def decoder(tpe: Type): Instance = instances.map(_.decoder).find(_.tpe =:= tpe).getOrElse(fail(tpe))
  }

  private[this] def membersFromPrimaryConstr(tpe: Type): Option[List[Member]] = tpe.decls.collectFirst {
    case m: MethodSymbol if m.isPrimaryConstructor => m.paramLists.flatten.map { field =>
      val asf = tpe.decl(field.name).asMethod.returnType.asSeenFrom(tpe, tpe.typeSymbol)

      Member(
        field.name.toTermName,
        field.name.decodedName.toString,
        asf,
        TermName(s"encoder_${field.name.decodedName.toString}"),
        TermName(s"decoder_${field.name.decodedName.toString}")
      )
    }
  }

  private[this] def productRepr(tpe: Type): Option[ProductRepr] =
    membersFromPrimaryConstr(tpe).map(ProductRepr(_))

}
