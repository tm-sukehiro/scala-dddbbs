name := """scala-dddbbs"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "org.sisioh" %% "scala-dddbase-core" % "0.2.9"
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "2.4.2",
  "com.h2database"  %  "h2"                % "1.4.192",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.7"
)
libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-orm"      % "2.2.0",
  "com.h2database"       %  "h2"              % "1.4.+",
  "ch.qos.logback"       %  "logback-classic" % "1.1.+"
)