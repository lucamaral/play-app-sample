import com.typesafe.config._
import play.Project._
import com.typesafe.sbt.packager.SettingsHelper._

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

name := conf.getString("application.name")

organization := conf.getString("application.organization")

version := conf.getString("application.version")

libraryDependencies ++= Seq(
  javaCore,
  "br.com.handit" % "maven-app-sample" % conf.getString("application.version")
)

resolvers += conf.getString("dependencies.repository.name") at conf.getString("dependencies.repository.url")

playJavaSettings

// publish to nexus standalone application.zip
publishTo := Some(conf.getString("deploy.repository.name") at conf.getString("deploy.repository.url"))

credentials += Credentials(Path.userHome / ".ivy2" / conf.getString("credentials.file.name"))

crossPaths := false

lazy val dist = com.typesafe.sbt.SbtNativePackager.NativePackagerKeys.dist

publish <<= (publish) dependsOn  dist

publishLocal <<= (publishLocal) dependsOn dist

val distHack = TaskKey[File]("dist-hack", "Hack to publish dist")

artifact in distHack ~= { (art: Artifact) => art.copy(`type` = "zip", extension = "zip") }

val distHackSettings = Seq[Setting[_]] (
  distHack <<= (target in Universal, normalizedName, version) map { (targetDir, id, version) =>
    val packageName = "%s-%s" format(id, version)
    targetDir / (packageName + ".zip")
  }) ++ Seq(addArtifact(artifact in distHack, distHack).settings: _*)

seq(distHackSettings: _*)

publishArtifact in (Compile, packageBin) := false

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := false
