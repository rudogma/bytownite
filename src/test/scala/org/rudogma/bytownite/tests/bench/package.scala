package org.rudogma.bytownite.tests

import scala.collection.mutable

import superquants._
import superquants.longprecision.time._

package object bench {

  def bench(title:String = "default", count:Int=1, measureAll:Boolean = false, sleep:Milliseconds = 0.millis)(f:  => Unit): Long ={

    var total_duration = 0L

    var i = 0

    var measures = mutable.Buffer.empty[Long]

    while( i < count){

      val started_at = System.currentTimeMillis()

      f

      val duration = System.currentTimeMillis() - started_at

      total_duration += duration

      if(measureAll){
        measures.append(duration)
      }

      if(sleep > 0){
        Thread.sleep(sleep)
      }

      i = i + 1
    }

    for( (measure,index) <- measures.zipWithIndex){
      println(s"[bench][${title}][run#${i}] ended in ${measure}ms")
    }

    println("[bench][%s][runs=%d] ended in %dms" format (title, count, total_duration))

    total_duration
  }
}
