package org.rudogma.bytownite.encoders

import org.rudogma.bytownite.HeaderBased
import org.rudogma.bytownite.utils.Bits


trait Encoder[@specialized T] {
  def encode(value:T):Array[Byte]
  def isNullable:Boolean
}

