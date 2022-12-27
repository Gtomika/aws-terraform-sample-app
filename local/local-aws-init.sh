chmod +x local-aws-init.sh

echo "Creating book images S3 bucket"
awslocal s3 mb s3://epam-cloudx-book-cover-images

echo "Creating book data Dynamo DB table"
awslocal dynamodb create-table \
    --table-name EpamCloudxBookData \
    --attribute-definitions AttributeName=Isbn,AttributeType=S AttributeName=Title,AttributeType=S AttributeName=Author,AttributeType=S AttributeName=PublishYear,AttributeType=N \
    --key-schema AttributeName=Isbn,KeyType=HASH