from argparse import ArgumentParser
from os import getenv
from pyspark.sql import SparkSession

#
# All these environment variables will be set for you.
#

bucket = getenv('JOB_BUCKET')  # e.g. s3://dig-analysis-data
method = getenv('JOB_METHOD')  # e.g. $method$
stage = getenv('JOB_STAGE')  # e.g. $stage$
job_name = getenv('JOB_NAME')  # e.g. T2D
job_prefix = getenv('JOB_PREFIX')  # e.g. out/$method$/$stage$/T2D

print(bucket)
print(method)
print(stage)
print(job_name)
print(job_prefix)

#
# Job steps can pass arguments to the scripts.
#

# build argument
opts = ArgumentParser()
opts.add_argument('phenotype')

# args.phenotype will be set
args = opts.parse_args()

# should show args.phenotype
print(args)

#
# Run the spark job.
#

# PySpark steps need to create a spark session
spark = SparkSession.builder.appName(method).getOrCreate()

# TODO: spark job here...

# done
spark.stop()
