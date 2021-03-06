name := "freezer"

version := "0.1"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
    "junit" % "junit" % "4.11" % "test"
)

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )