package $group$.$artifact$

import org.broadinstitute.dig.aggregator.core._
import org.broadinstitute.dig.aws._
import org.broadinstitute.dig.aws.emr._

/** This is the entry point for your method.
  *
  * All that needs to be done here is to add stages to the method and
  * define the provenance information.
  *
  * When you are ready to run it, use SBT from the CLI:
  *
  *   sbt run
  *
  * See the README in dig-aggregator-core for a complete list of CLI
  * arguments.
  */
object $method$ extends Method {

  /** Every method needs provenance. This is recorded for every run so it
    * is possible to go back and see the exact code used to generate the
    * output.
    */
  override val provenance: Option[Provenance] = {
    Provenance.fromResource("version.properties")
  }

  /** Add all stages used in this method here. Stages must be added in the
    * order they should be executed.
    */
  override def initStages(): Unit = {
    addStage($stage$)
  }
}
