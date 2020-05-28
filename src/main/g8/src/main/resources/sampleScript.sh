#!/bin/bash -xe

#
# You can also pass command line arguments to the script from your stage.
#

echo "Argument passed: \$1"

#
# You have access to the AWS CLI to copy/read data from S3.
#

aws s3 ls "out/metaanalysis/trans-ethnic/\$1/" --recursive

#
# You can also use the hadoop command.
#
