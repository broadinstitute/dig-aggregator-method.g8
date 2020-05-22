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
object $stage$ extends Stage {

  /** Cluster configuration used when running this stage. The super
    * class already has a default configuration defined, so it's easier
    * to just override parts of it here.
    */
  override val cluster: ClusterDef = super.cluster.copy(
    instances = 1
  )

  /** Whenever one of these dependencies is added to S3 or updated,
    * this stage will run.
    */
  override val dependencies: Seq[Input.Source] = Seq(
    Input.Source.Success("out/metaanalysis/trans-ethnic/")
  )

  /** For every dependency that is new/updated, this function is called
    * and should return the outputs that need to be built.
    *
    * The outputs are just strings that you define for your stage. They
    * can be anything you like, but should generally not change.
    *
    * A single input can return one or more output.
    *
    * Each input is an S3 object's key and e-tag (checksum). The key is
    * should be pattern matched with a Glob object to determine which
    * output(s) to return.
    */
  override def getOutputs(input: Input): Outputs = {
    val metaAnalysis = Glob("out/metaanalysis/trans-ethnic/*/...")

    input.key match {
      case metaAnalysis(phenotype) => Outputs.Named(phenotype)
    }
  }

  /** For every output returned by getOutputs, this function is called and
    * should return a sequence of steps that will be run - in order - on the
    * instantiated cluster.
    *
    * This is almost always either a PySpark step (spark job) or a Script
    * step (i.e. bash, perl, python, ruby).
    */
  override def getJob(output: String): Seq[JobStep] = {

    /* All job steps require a URI to a location in S3 where the script can
     * be read from. The resourceURI function uploads the resource in the JAR
     * to a known location in S3 and return the URI to it.
     */
    val sampleSparkJob = resourceURI("sampleSparkJob.py")
    val sampleScript = resourceURI("sampleScript.sh")

    // we used the phenotype to process as the output
    val phenotype = output

    // list of steps to execute for this job
    Seq(
      JobStep.PySpark(sampleSparkJob, phenotype),
      JobStep.Script(sampleScript, phenotype)
    )
  }
}
