package $group$.$artifact$

import org.broadinstitute.dig.aggregator.core._
import org.broadinstitute.dig.aws._
import org.broadinstitute.dig.aws.emr._

/** A stage
  */
object $stage$ extends Stage {

  /* Cluster configuration used when running this stage.
   */
  override def cluster: ClusterDef = super.cluster

  /** Whenever one of these dependencies is added to S3 or updated,
    * this stage will run.
    */
  override val dependencies: Seq[Input.Source] = Seq(
    // example dataset source
    Input.Source.Dataset("variants/"),
    // example method output source
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
    val variants = Glob("variants/*/*/...")
    val metaAnalysis = Glob("out/metaanalysis/trans-ethnic/*/...")

    input.key match {
      case variants(dataset, phenotype) => Outputs.Named(phenotype)
      case metaAnalysis(phenotype)      => Outputs.Named(phenotype)
    }
  }

  /** For every output returned by getOutputs, this function is called and
    * should return a series of job steps that will be run on the cluster
    * instantiated.
    *
    * This is almost always either a PySpark step (spark job) or a Script
    * step (i.e. bash, perl, python, ruby).
    */
  override def getJob(output: String): Seq[JobStep] = {

    /* All job steps require a URI to a location in S3 where the script can
     * be read from. The resourceURI function uploads the resource in the JAR
     * to a known location in S3 and return the URI to it.
     */
    val sampleSpark = resourceURI("sampleSparkJob.py")
    val sampleScript = resourceURI("sampleScript.sh")

    // we used the phenotype to process as the output
    val phenotype = output

    // list of steps to execute for this job
    Seq(
      JobStep.PySpark(sampleSpark, phenotype),
      JobStep.Script(sampleScript, phenotype)
    )
  }
}
