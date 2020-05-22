#!/bin/bash -xe

#
# Several environment variables are already set for you.
#

echo "\${BUCKET}"  # e.g. s3://dig-analysis-data
echo "\${METHOD}"  # e.g. $method$
echo "\${STAGE}"  # e.g. $stage$
echo "\${OUTPUT}"  # e.g. T2D
echo "\${PREFIX}"  # e.g. out/$method$/$stage$/T2D

#
# You can also pass command line arguments to the script from your stage.
#

PHENOTYPE="\$1"

#
# You have access to the AWS CLI to copy/read data from S3.
#

aws s3 cp "\${BUCKET}/bin/samtools/htslib-1.9.tar.gz" .
tar zxf htslib-1.9.tar.gz
