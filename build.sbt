import play.Project._

name := "play_app_sample"

version := "0.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  "br.com.handit" % "maven-app-sample" % "0.0.0-SNAPSHOT"
)

playJavaSettings

resolvers += "Nexus Repository" at "http://192.168.25.24:8081/nexus/content/groups/public/"
