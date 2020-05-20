val Orgs = new {
  val DIG = "org.broadinstitute.dig"
}

val Versions = new {
  val Scala = "2.13.2"
  val DigAggregator = "0.3-SNAPSHOT"
  val DigAws = "0.3-SNAPSHOT"
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
  Orgs.DIG %% "dig-aggregator-core" % Versions.DigAggregator,
  Orgs.DIG %% "dig-aws" % Versions.DigAws
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
  streams.value.log.info(s"Writing version info to \$file...")

  // collect git versioning information
  val branch = git.gitCurrentBranch.value
  val lastCommit = git.gitHeadCommit.value
  val describedVersion = git.gitDescribedVersion.value
  val anyUncommittedChanges = git.gitUncommittedChanges.value
  val remoteUrl = (scmInfo in ThisBuild).value.map(_.browseUrl.toString)
  val buildDate = java.time.Instant.now

  val file = (resourceManaged in Compile).value / "versionInfo.properties"

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

  // add the file to the jar
  Seq(file)
}
