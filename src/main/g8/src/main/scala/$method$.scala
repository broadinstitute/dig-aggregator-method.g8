package $group$.$artifact$

import org.broadinstitute.dig.aggregator.core._
import org.broadinstitute.dig.aws._
import org.broadinstitute.dig.aws.emr._

/** This is the entry point for your method.
  *
  * The main() function is already written for you. All that
  * needs to be done here is to add each of the stages for
  * your method in the order they should be executed.
  *
  * When you are ready to run it, use SBT from the CLI:
  *
  * sbt run org.broadinstitute.org.aggregator.methods.$method$
  *
  * This will output the stages that need to be executed due to
  * new or updated dependencies.
  *
  * If you provide --yes on the command line, the stage(s) will
  * actually run. Providing --test will redirect any outputs to
  * a test location assuming the Spark jobs/scripts are written
  * to understand it.
  */
object $method$ extends Method {
  addStage($stage$)
}
