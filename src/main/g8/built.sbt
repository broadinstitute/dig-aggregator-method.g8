val Orgs = new {
  val DIG = "org.broadinstitute.dig"
}

val Versions = new {
  val Scala = "2.13.2"
  val DigAggregator = "0.3.0-SNAPSHOT"
}

// set the version of scala to compile with
scalaVersion := Versions.Scala

// add scala compile flags
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Ywarn-value-discard"
)

// add required libraries
libraryDependencies ++= Seq(
  Orgs.DIG %% "dig-aggregator-core" % Versions.DigAggregator
)

// set the oranization this method belongs to
organization := "$organization$"

// entry point when running this method
mainClass := Some("$group$.$artifact$.$method$")

// enables buildInfo, which bakes git version info into the jar
enablePlugins(GitVersioning)

// get the buildInfo task
val buildInfoTask = taskKey[Seq[File]]("buildInfo")

// define execution code for task
buildInfoTask := {
  val file = (resourceManaged in Compile).value / "versionInfo.properties"

  // log where the properties will be written to
  streams.value.log.info(s"Writing version info to \$file...")

  // collect git versioning information
  val branch = git.gitCurrentBranch.value
  val lastCommit = git.gitHeadCommit.value
  val describedVersion = git.gitDescribedVersion.value
  val anyUncommittedChanges = git.gitUncommittedChanges.value
  val remoteUrl = (scmInfo in ThisBuild).value.map(_.browseUrl.toString)
  val buildDate = java.time.Instant.now

  // build properties content
  val contents =
    s"""|branch=\${branch}
        |lastCommit=\${lastCommit.getOrElse("")}
        |uncommittedChanges=\${anyUncommittedChanges}
        |buildDate=\${buildDate}
        |remoteUrl=\${remoteUrl.getOrElse("")}
        |""".stripMargin

  // output the version information from git to versionInfo.properties
  IO.write(file, contents)
  Seq(file)
}

// add the build info task output to resources
(resourceGenerators in Compile) += buildInfoTask.taskValue
