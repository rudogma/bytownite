package org.rudogma.bytownite

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

@compileTimeOnly("enable macro paradise to expand macro annotations")
final class Bytownite extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro BytowniteImpl.transform_impl
}


class BytowniteImpl[Context <: whitebox.Context](val c:Context, val debug:Boolean) {

  import c.universe._

  def generateNewCompanion(classDef: c.universe.ClassDef, companion:Option[c.universe.ModuleDef], debug:Boolean = false): Tree ={

    val deriveEncoder = if(debug) q"debug_deriveEncoder" else q"deriveEncoder"
    val deriveDecoder = if(debug) q"debug_deriveDecoder" else q"deriveDecoder"

    val result = q"""
           object ${classDef.name.toTermName} {

           import _root_.org.rudogma.bytownite._
           import _root_.org.rudogma.bytownite.encoders._
           import _root_.org.rudogma.bytownite.decoders._
           import _root_.org.rudogma.bytownite.Implicits._

            implicit val encoder:Encoder[${classDef.name.toTypeName}] = $deriveEncoder
            implicit val decoder:Decoder[${classDef.name.toTypeName}] = $deriveDecoder


            }
         """

//    println("----Implicit derived encoder")
//    println(result)

    result
  }


}

object BytowniteImpl {
  def isDebug(mods: whitebox.Context#Modifiers):Boolean = mods.annotations.find( _.toString() == "new Debug()").map(_ => true).getOrElse(false)

  def transform_impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {

    import c.universe._

    var debug = false

    val result:List[Tree] = annottees.map(_.tree).toList match {

      case expr @ (classDef:ClassDef) :: Nil =>

        debug = isDebug(classDef.mods)

        if(debug){
          println(s"[building for ${classDef.name}] Exists: class only, debug: ${debug}")
        }



        List(classDef, new BytowniteImpl[c.type](c, debug).generateNewCompanion(classDef, None, debug))

      case expr @ (classDef:ClassDef) :: (companion:ModuleDef) :: Nil =>

        debug = isDebug(classDef.mods)

        if(debug){
          println(s"[building for ${classDef.name}] Exists: class+companion, debug: ${debug}")
        }


        List(classDef, new BytowniteImpl[c.type](c, debug).generateNewCompanion(classDef, Some(companion), debug))
      case e =>

        c.abort(c.enclosingPosition, s"Macro @BuildService not supported for tree: ${e.head.getClass}")
        ???
    }

    var block = q"{ ..$result }"

    c.Expr[Any](block)
  }
}

