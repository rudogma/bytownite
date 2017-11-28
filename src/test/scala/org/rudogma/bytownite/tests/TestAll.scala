package org.rudogma.bytownite.tests

import org.rudogma.bytownite._
import org.rudogma.bytownite.utils.UnsyncByteArrayInputStream
import org.scalatest.{FlatSpec, Matchers}
import org.rudogma.equalator._
import org.rudogma.equalator.Implicits._
import org.rudogma.bytownite.Implicits._
import org.rudogma.bytownite.decoders.Decoder
import org.rudogma.bytownite.encoders.Encoder

class TestAll extends FlatSpec with Matchers {

  def makeTest[T : Encoder : Decoder : Equalator](value:T): Unit ={

    val eq = implicitly[Equalator[T]]

    val s1 = implicitly[Encoder[T]]
    val s2 = implicitly[Decoder[T]]

    val serializedBytes = s1.encode(value)

    val extractedValue = s2.extract(new UnsyncByteArrayInputStream(serializedBytes))


    eq.deepEquals(extractedValue.value, value) match {
      case Left(e) =>

        println(s"deepEquals failed with: ${e.message}")
        println(s"Input: ${value}")
        println(s"Extracted: ${extractedValue.value}")
        println(s"[encoded.bytes][${serializedBytes.grouped(40).map(_.mkString(",")).mkString("\n")}]")

        System.out.flush()

        e.printStackTrace()
      case Right(x) =>
        //pass
    }
  }


  "Test" should "work" in {

    makeTest(true)
    makeTest(false)

    makeTest(1.toByte)
    makeTest(1.toShort)
    makeTest(1)
    makeTest(1L)

    makeTest(1.1f)
    makeTest(1.1D)

    makeTest(Array[Byte](0,1,2,3,4))

    makeTest(TMicro())
    makeTest(TMedium())
    makeTest(TBig())
  }
}


