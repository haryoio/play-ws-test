name := """play-ws-test"""
organization := "com.haryoiro"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaJdbc,
  javaWs,
  filters,
  guice,
  evolutions,

  "org.projectlombok" % "lombok" % "1.18.34",
  "com.h2database" % "h2" % "1.4.200",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.7.9",
)

scalaVersion := "2.13.10"

libraryDependencies += guice
