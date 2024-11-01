import VersionHelper.{versionFmt, fallbackVersion}

// Lets me depend on Maven Central artifacts immediately without waiting
resolvers ++= Resolver.sonatypeOssRepos("public")

ThisBuild / scalaVersion := Versions.Scala_3

lazy val sl = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= List(
      "com.raquo" %%% "laminar" % Versions.Laminar % "provided"
    ),
    (Compile / doc / scalacOptions) ++= Seq(
      "-no-link-warnings" // Suppress scaladoc "Could not find any member to link for" warnings
    ),
    scalacOptions ++= Seq(
      "-deprecation"
    ),
    scalacOptions ++= sys.env.get("CI").map { _ =>
      val localSourcesPath = (LocalRootProject / baseDirectory).value.toURI
      val remoteSourcesPath =
        s"https://raw.githubusercontent.com/ppbustamante/laminar-material-web-components/${git.gitHeadCommit.value.get}/"
      val sourcesOptionName =
        if (scalaVersion.value.startsWith("2.")) "-P:scalajs:mapSourceURI"
        else "-scalajs-mapSourceURI"

      s"${sourcesOptionName}:$localSourcesPath->$remoteSourcesPath"
    }
  )
  .settings(
    name := "Laminar Material Web",
    normalizedName := "laminar-material-web",
    organization := "cl.closure",
    homepage := Some(
      url("https://github.com/ppbustamante/laminar-material-web-components")
    ),
    licenses += ("MIT", url(
      "https://github.com/ppbustamante/laminar-material-web-components/blob/master/LICENSE.md"
    )),
    scmInfo := Some(
      ScmInfo(
        url(
          "https://github.com/ppbustamante/laminar-material-web-components"
        ),
        "scm:git@github.com:ppbustamante/laminar-material-web-components.git"
      )
    ),
    developers := List(
      Developer(
        id = "ppbustamante",
        name = "Pablo Bustamante",
        email = "ppbb15@gmail.com",
        url = url("https://github.com/ppbustamante")
      )
    ),
    (Test / publishArtifact) := false,
    pomIncludeRepository := { _ => false },
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
  )
