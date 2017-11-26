val commonSettings = Seq(
  organization := "com.elkozmon",
  licenses += ("GNU Affero GPL V3", url("http://www.gnu.org/licenses/agpl-3.0.html")),
  developers := List(
    Developer(
      id = "elkozmon",
      name = "Ľuboš Kozmon",
      email = "lubosh91@gmail.com",
      url = url("http://www.elkozmon.com")
    )
  ),
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Ywarn-adapted-args",
    "-Ywarn-value-discard",
    "-Ywarn-inaccessible",
    "-Ywarn-dead-code"
  ),
  wartremoverErrors ++= Warts.unsafe
)

val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "zoonavigator-core",
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "joda-time" % "joda-time" % "2.9.9",
      "org.apache.curator" % "curator-framework" % "2.12.0",
      "org.apache.curator" % "curator-test" % "2.12.0" % Test,
      "com.chuusai" %% "shapeless" % "2.3.2",
      "org.scalatest" %% "scalatest" % "3.0.4" % Test
    )
  )

val play = (project in file("play"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := "zoonavigator-play",
    libraryDependencies ++= Seq(
      filters,
      "org.typelevel" %% "cats" % "0.9.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.google.guava" % "guava" % "16.0.1",
      "com.softwaremill.macwire" %% "macros" % "2.3.0" % Provided,
      "com.softwaremill.macwire" %% "util" % "2.3.0"
    ),
    wartremoverWarnings ++= Seq(
      PlayWart.AssetsObject,
      PlayWart.CookiesPartial,
      PlayWart.FlashPartial,
      PlayWart.FormPartial,
      PlayWart.HeadersPartial,
      PlayWart.JavaApi,
      PlayWart.JsLookupResultPartial,
      PlayWart.JsReadablePartial,
      PlayWart.LangObject,
      PlayWart.MessagesObject,
      PlayWart.SessionPartial,
      PlayWart.TypedMapPartial,
      PlayWart.WSResponsePartial
    )
  )
  .dependsOn(core)
