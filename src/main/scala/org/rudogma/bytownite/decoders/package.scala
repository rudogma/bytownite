package org.rudogma.bytownite

package object decoders {

  case class Extracted[@specialized T](var value:T, var bytes:Int)
}
