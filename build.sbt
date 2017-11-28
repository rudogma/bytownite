organization := "org.rudogma"
name := "bytownite"

version in ThisBuild := "0.9"
isSnapshot in ThisBuild := true

val SCALA_VERSION = "2.12.2"
val paradiseVersion = "2.1.1"

(scalaVersion in ThisBuild) := SCALA_VERSION


addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)


lazy val mainDependencies = Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.2",
  "org.rudogma" %% "supertagged" % "1.4"
)

lazy val testDependencies = Seq(
  "org.rudogma" %% "superquants" % "0.9" % "test",
  "org.rudogma" %% "equalator" % "1.2" % "test",


  "org.spire-math" %% "spire" % "0.13.0" % "test",
  "com.chuusai" %% "shapeless" % "2.3.2" % "test",

  "org.scalactic" %% "scalactic" % "3.0.0" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)


libraryDependencies ++= mainDependencies ++ testDependencies