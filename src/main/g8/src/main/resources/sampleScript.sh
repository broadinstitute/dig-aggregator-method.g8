#!/bin/bash -xe

#
# Several environment variables are already set for you.
#

echo "\${JOB_BUCKET}"  # e.g. s3://dig-analysis-data
echo "\${JOB_METHOD}"  # e.g. $method$
echo "\${JOB_STAGE}"   # e.g. $stage$
echo "\${JOB_NAME}"    # e.g. T2D
echo "\${JOB_PREFIX}"  # e.g. out/$method$/$stage$/T2D

#
# You can also pass command line arguments to the script from your stage.
#

PHENOTYPE="\$1"

#
# You have access to the AWS CLI to copy/read data from S3.
#

aws s3 cp "\${JOB_BUCKET}/bin/samtools/htslib-1.9.tar.gz" .
tar zxf htslib-1.9.tar.gz
