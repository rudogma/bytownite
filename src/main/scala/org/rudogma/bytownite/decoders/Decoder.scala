package org.rudogma.bytownite.decoders

import java.io.InputStream

import org.rudogma.bytownite
import org.rudogma.bytownite.HeaderBased
import org.rudogma.bytownite.utils.Bits


trait Decoder[@specialized T] {
  def extract(input:InputStream):Extracted[T]
  def isNullable:Boolean
}

