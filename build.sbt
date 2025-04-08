
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"
import _root_.caliban.tools.Codegen


lazy val root = (project in file("."))
  .enablePlugins(CalibanPlugin)
  .settings(
    name := "schema-issue",
    scalacOptions ++= Seq("-Xmax-inlines", "5000"),
    libraryDependencies ++= List(
      "com.github.ghostdogpr" %% "caliban" % "2.10.0",
      "com.github.ghostdogpr" %% "caliban-quick" % "2.10.0"
    ),
    Compile / caliban / calibanSettings ++=  List(
      calibanSetting(
        file("projexion.graphql")
      )(
        _.genType(Codegen.GenType.Schema)
          .clientName("PROJEXION")
          .addDerives(true)
          .effect("zio.query.TaskQuery")
          .imports(
            List(
              "java.time.LocalDate",
              "java.time.ZonedDateTime"
            ):_*
          )
          .scalarMapping("ID" -> "String")
          .packageName(
            s"com.projexion.platform.api.graphql.api"
          )
      )
    )
  )
