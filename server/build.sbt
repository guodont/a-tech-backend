name := """server"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayJava)

val mysqlConnectorVersion = "5.1.32"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % mysqlConnectorVersion,
  "cn.jpush.api" % "jpush-client" % "3.2.9",
  "com.qiniu" % "qiniu-java-sdk" % "7.0.0"
)

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)

resolvers ++= Seq(
  "Primefaces" at "http://mvnrepository.com"
)