#!/bin/bash -xe

#
# Several environment variables are already set for you.
#

echo "\${S3_BUCKET}"  # e.g. s3://dig-analysis-data
echo "\${METHOD_NAME}"  # e.g. $method$
echo "\${STAGE_NAME}"  # e.g. $stage$
echo "\${SESSION_NAME}"  # e.g. $method$.$stage$
echo "\${STAGE_OUTPUT}"  # e.g. T2D
echo "\${OUTPUT_PREFIX}"  # e.g. out/$method$/$stage$

#
# You can also pass command line arguments to the script from your stage.
#

PHENOTYPE="\$1"

#
# You have access to the AWS CLI to copy/read data from S3.
#

aws s3 cp "\${S3_BUCKET}/bin/samtools/htslib-1.9.tar.gz" .
tar zxf htslib-1.9.tar.gz
