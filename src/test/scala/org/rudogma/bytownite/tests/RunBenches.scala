package org.rudogma.bytownite.tests

import scala.util.Random
import org.rudogma.bytownite._
import spire.syntax.cfor
import org.rudogma.bytownite.Implicits._
import org.rudogma.bytownite.utils.UnsyncByteArrayInputStream
import superquants._
import superquants.render._
import longprecision.time._
import longprecision.binary._
import org.rudogma.bytownite.decoders.Decoder
import org.rudogma.bytownite.encoders.Encoder
import shapeless.{::, HNil}

import scala.reflect.ClassTag

object RunBenches extends App {


  val limit = 1000 * 1000
  val benchDecoding = false


  var total = 0

  val random = new Random()

  /**
    * Тотал + 1 - чтобы избежать кеширования результата
    */

  run(5){
    total += 1
    TBig(t2 = TMicro(t2_intValue = total))
  }
  run(){
    total += 1
    TMicro(t2_intValue = total)
  }
  run(5){
    total += 1
    TMedium(intValue = total)
  }
  run(){
    total += 1
    total
  }


  println(s"total: ${total}")




  def run[E : ClassTag: Encoder : Decoder](count:Int = 10)(next: => E):Unit = {

    Thread.sleep(1000)

    val data = new Array[E](limit)

    cfor.cfor(0)( _ < limit, _ + 1){ i =>
      data(i) = next
    }



    var totalBytesEncoded:Long = 0
    var totalBytesDecoded:Long = 0

    val encoder = implicitly[Encoder[E]]
    val decoder = implicitly[Decoder[E]]

    val startedAt = System.currentTimeMillis()

    val duration = bench.bench(measureAll = true, count = count, sleep = 100.millis){
      cfor.cfor(0)( _ < limit, _ + 1){ i =>
        val item = data(i)//next

        val encoded = encoder.encode(item)

        totalBytesEncoded += encoded.length

        if(benchDecoding){
          val decoded = decoder.extract(new UnsyncByteArrayInputStream(encoded))
          totalBytesDecoded += decoded.bytes
        }
      }
    }

    println(s"in: ${duration.millis.pretty[Seconds :: Milliseconds :: HNil]}")
    println(s"totalBytesEncoded: ${totalBytesEncoded.bytes.pretty[MegaBytes :: HNil]}, decoded: ${totalBytesDecoded.bytes.pretty[MegaBytes :: HNil]}")

    val encodedSpeed = (totalBytesEncoded / 1024 / 1024) / (duration / 1000.toFloat)

    println(s"encodedSpeed: ${encodedSpeed} MB/s")

  }
}
