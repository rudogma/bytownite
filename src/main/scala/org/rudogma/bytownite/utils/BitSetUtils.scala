package org.rudogma.bytownite.utils

object BitSetUtils {

  def toArrayBoolean(bitSet:BitSet):Array[Boolean] = {
    (0 until bitSet.getNumBits.toInt).map(bitSet.fastGet).toArray[Boolean]
  }
}
