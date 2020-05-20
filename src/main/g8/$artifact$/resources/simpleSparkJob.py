from argparse import ArgumentParser
from os import getenv
from pyspark.sql import SparkSession

#
# All these environment variables will be set for you.
#

s3_bucket = getenv('S3_BUCKET')  # e.g. s3://dig-analysis-data
method_name = getenv('METHOD_NAME')  # e.g. $method$
stage_name = getenv('STAGE_NAME')  # e.g. $stage$
session_name = getenv('SESSION_NAME')  # e.g. $method$.$stage$
stage_output = getenv('STAGE_OUTPUT')  # e.g. T2D
prefix_out = getenv('OUTPUT_PREFIX')  # e.g. out/$method$/$stage$

#
# Job steps can pass arguments to the scripts.
#

# build argument
opts = ArgumentParser()
opts.add_argument('phenotype')

# args.phenotype will be set
args = opts.parse_args()

#
# Run the spark job.
#

# PySpark steps need to create a spark session
spark = SparkSession.builder.appName(session_name).getOrCreate()

# load some input data
df = spark.read.json(f'{s3_bucket}/variants/*/{args.phenotype}/part-*')

# do some processing to it
df = df.filter(df.pValue < 5e-8)

# write out the results
df.write.json(f'{s3_bucket}/{prefix_out}/{args.phenotype}')

# done
spark.stop()
