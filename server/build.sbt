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
  "org.springframework" % "spring-aop" % "4.0.3.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.3.RELEASE",
  "org.springframework" % "spring-test" % "4.0.3.RELEASE",
  "org.springframework.security" % "spring-security-config" % "3.2.3.RELEASE",
  "org.springframework.security" % "spring-security-core" % "3.2.3.RELEASE",
  "org.springframework.security.oauth" % "spring-security-oauth2" % "2.0.2.RELEASE"
)

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)
