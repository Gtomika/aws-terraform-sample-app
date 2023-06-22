#!/bin/bash
chmod +x local-aws-init.sh

echo "Creating book images S3 bucket"
awslocal s3 mb s3://cloudx-book-cover-images

echo "Creating book data Dynamo DB table"
awslocal dynamodb create-table \
    --table-name CloudxBookData \
    --attribute-definitions AttributeName=isbn,AttributeType=S \
    --key-schema AttributeName=isbn,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

echo "Creating customer data Dynamo DB table"
awslocal dynamodb create-table \
    --table-name CloudxCustomerData \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

echo "Create SNS topic for customer emails"
awslocal sns create-topic --name cloudx-book-notifications