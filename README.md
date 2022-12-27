## AWS + Terraform sample application

Simple Book API that uses several AWS components:

 - EC2 instance to run the app.
 - DynamoDB to store book data.
 - S3 bucket to store book cover images.
 - SES to send book event notification emails.
 - VPN + subnet + elastic IP.

All of these resources are provisioned using Terraform, which is applied through the 
GitLab CI/CD pipeline.

## Run locally

When running locally, all AWS services are mocked out using the docker container 
``localstack``. You need to set the following environmental variables:

```
AWS_REGION=eu-central-1;
ENVIRONMENT=dev;
AWS_S3_BOOK_IMAGES_BUCKET_NAME=epam-cloudx-book-cover-images;
AWS_DYNAMODB_BOOK_DATA_TABLE_NAME=EpamCloudxBookData
```

Then set up the Docker with CLI or IntelliJ support.

```
docker-compose -f ./local/docker-compose.yml up
```

Finally, the application can be started and it will use the ``localstack`` 
instead of real AWS.
