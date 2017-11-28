package org.rudogma.bytownite

package object tests {

  trait TArray[@specialized T]{
    var array:Array[T]
  }

  @Bytownite
  case class TArrayOfByte(var array:Array[Byte]) extends TArray[Byte]

  @Bytownite
  case class TArrayOfInt(var array:Array[Int]) extends TArray[Int]

  @Bytownite
  case class TMedium(
                      var byteValue:Byte = 111,
                      var shortValue:Short = 12232,
                      var intValue:Int = 123121202,
                      var longValue1:Long = 12931231231221312L,
                      var longValue2:Long = 12931231231221312L,
                      var longValue3:Long = 12931231231221312L,
                      var longValue4:Long = 12931231231221312L,
                      var longValue5:Long = 12931231231221312L,
                      var longValue6:Long = 12931231231221312L,
                      var longValue7:Long = 12931231231221312L,
                      var longValue8:Long = 12931231231221312L,
                      var longValue9:Long = 12931231231221312L,
                      var longValue0:Long = 12931231231221312L,
                      var longValue11:Long = 12931231231221312L,
                      var longValue22:Long = 12931231231221312L,
                      var longValue33:Long = 12931231231221312L,
                      var longValue44:Long = 12931231231221312L,
                      var longValue55:Long = 12931231231221312L,
                      var longValue66:Long = 12931231231221312L,
                      var longValue77:Long = 12931231231221312L,
                      var longValue88:Long = 12931231231221312L,
                      var longValue99:Long = 12931231231221312L,
                      var longValue00:Long = 12931231231221312L,
                      var longValue111:Long = 12931231231221312L,
                      var longValue222:Long = 12931231231221312L,
                      var longValue333:Long = 12931231231221312L,
                      var longValue444:Long = 12931231231221312L,
                      var longValue555:Long = 12931231231221312L
                    )

  @Bytownite
  case class TBig(
                   var byteValue:Byte = 111,
                   var shortValue:Short = 12232,
                   var intValue:Int = 123121202,
                   var longValue:Long = 12931231231221312L,

                   var booleanValue:Boolean = true,

                   var stringValue:String = "Some string value",

                   //  @Compress
                   var stringValue2:String = "Some some some some some some some some some some some some some some some some",

                   var emptyString:String = "",

                   //  @Compress
                   var emptyCompressedString:String = "",

                   var stringNullValue:String = null,

                   var a1:Array[Byte] = Array[Byte](0,1,2,3,4, Byte.MinValue, Byte.MaxValue),
                   var a2:Array[Short] = Array[Short](0,1,2,3,4, Short.MinValue, Short.MaxValue),
                   var a3:Array[Int] = Array[Int](0,1,2,3,4, Int.MinValue, Int.MaxValue),
                   var a4:Array[Long] = Array[Long](0,1,2,3,4, Long.MinValue, Long.MaxValue),
                   var a5:Array[Char] = Array[Char](0,1,2,3,4, Char.MinValue, Char.MaxValue),

                   var a1_null:Array[Byte] = null,
                   var a2_null:Array[Int] = null,

                   var t2:TMicro = TMicro(),
                   var t2_null:TMicro = null,
//                   var t2_null:TMicro = TMicro(t2_byteValue = 5),

                   var string_array:Array[String] = Array("H1", "h2", "h3", "h4"),

                   var obj_array:Array[TMicro] = null,
                   var obj_array_null:Array[TMicro] = null,

                   var obj_array_t2:Array[TMicro] = Array(TMicro(), TMicro()),

                   var array_array:Array[Array[String]] = Array( Array("1","2"), Array("3","4"))

                 )
  @Bytownite
  case class TMicro(
                     t2_byteValue:Byte = 111,
                     t2_shortValue:Short = 12232,
                     t2_intValue:Int = 123121202,
                     t2_longValue:Long = 12931231231221312L
                   )
}
