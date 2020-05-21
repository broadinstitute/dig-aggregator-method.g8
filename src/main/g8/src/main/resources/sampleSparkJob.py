from argparse import ArgumentParser
from os import getenv
from pyspark.sql import SparkSession

#
# All these environment variables will be set for you.
#

s3_bucket = getenv('BUCKET')  # e.g. s3://dig-analysis-data
method_name = getenv('METHOD')  # e.g. $method$
stage_name = getenv('STAGE')  # e.g. $stage$
job_name = getenv('JOB')  # e.g. T2D
prefix_out = getenv('PREFIX')  # e.g. out/$method$/$stage$/T2D

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
spark = SparkSession.builder.appName(method_name).getOrCreate()

# load some input data
df = spark.read.json(f'{s3_bucket}/variants/*/{args.phenotype}/part-*')

# do some processing to it
df = df.filter(df.pValue < 5e-8)

# write out the results
df.write.json(f'{s3_bucket}/{prefix_out}')

# done
spark.stop()
