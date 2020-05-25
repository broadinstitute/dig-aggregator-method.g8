package $group$.$artifact$

import org.broadinstitute.dig.aggregator.core._
import org.broadinstitute.dig.aws._
import org.broadinstitute.dig.aws.emr._

/** This is a single stage in your method.
  *
  * Stages are one or more output jobs that can be run by your method
  * that are generally grouped together when they logically can be run
  * in parallel across multiple, identically configured clusters.
  *
  * For example, running METAL is 3 stages:
  *
  *   1. Partitioning variants from datasets
  *   2. Running METAL
  *   3. Producing plots of the METAL output
  *
  * Stages 2 and 3 must obviously be separated as the output of 2 is the
  * input of 3 (via Input.Source.Success).
  *
  * The separation of stages 1 and 2 is more nuanced. Partitioning could
  * be a single step in the job of running METAL, but it benefits greatly
  * from having a cluster with multiple nodes to distribute the work, while
  * METAL is just a single, large-node cluster.
  */
class $stage$(implicit context: Context) extends Stage {

  /** Cluster configuration used when running this stage. The super
    * class already has a default configuration defined, so it's easier
    * to just override parts of it here.
    */
  override val cluster: ClusterDef = super.cluster.copy(
    instances = 1
  )

  /** Input sources need to be declared so they can be used in rules.
    */
  val metaAnalysis: Input.Source =
    Input.Source.Success("out/metaanalysis/trans-ethnic/*/")

  /** When run, all the input sources here will be checked to see if they
    * are new or updated.
    */
  override val sources: Seq[Input.Source] = Seq(metaAnalysis)

  /** For every dependency that is new/updated, this partial function is
    * called, which maps the input sources to the outputs that should be
    * built.
    *
    * Each source can use wildcards to match arbitrary patterns in the input
    * key. These patterns are extracted in the rules and can be used when
    * generating the outputs.
    */
  override val rules: PartialFunction[Input, Outputs] = {
    case metaAnalysis(phenotype) => Outputs.Named(phenotype)
  }

  /** Once all the rules have been applied to the new and updated inputs,
    * each of the outputs needs to be build. This method returns the job
    * steps that should be performed on the cluster.
    */
  override def make(output: String): Seq[JobStep] = {

    /* All job steps require a URI to a location in S3 where the script can
     * be read from by the cluster. The resourceURI function uploads the
     * resource in the JAR to a unique location in S3 identified by the
     * method name, stage name, and the path of the resource in the JAR.
     */
    val sampleSparkJob = resourceURI("sampleSparkJob.py")
    val sampleScript = resourceURI("sampleScript.sh")

    // we used the phenotype as the output in rules
    val phenotype = output

    // list of steps to execute for this job
    Seq(
      JobStep.PySpark(sampleSparkJob, phenotype),
      JobStep.Script(sampleScript, phenotype)
    )
  }
}
