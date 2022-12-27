#!/bin/bash
chmod +x local-aws-init.sh

echo "Creating book images S3 bucket"
awslocal s3 mb s3://epam-cloudx-book-cover-images

echo "Creating book data Dynamo DB table"
awslocal dynamodb create-table \
    --table-name EpamCloudxBookData \
    --attribute-definitions AttributeName=isbn,AttributeType=S \
    --key-schema AttributeName=isbn,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

