## AWS + Terraform sample application

Simple Book API that uses several AWS components:

 - EC2 instance to run the app.
 - DynamoDB table to store book data.
 - S3 bucket to store book cover images (included bucket policy).
 - VPN, subnet, internet gateway, elastic IP, security group.
 - IAM role and policy with the principle of the least privilege.

All of these resources are provisioned using Terraform, which is applied through the 
GitLab CI/CD pipeline.

## Test

 - To run unit tests: ``./gradlew test``
 - To run integration tests: ``./gradlew integrationTest``

## Run locally

When running locally, all AWS services are mocked out using the docker container 
``localstack``. Set up the Docker with CLI or IntelliJ support.

```
docker-compose -f ./local/docker-compose.yml up
```

Finally, the application can be started, and it will use the ``localstack`` 
instead of real AWS. Make sure to start it with the Spring active profile 
``dev`` (can be done with IDE, property or environmental variable).

**Warning**: The localstack S3 don't seem to be working well. It returns 500 
internal error when trying to upload a file. Book image update is expected 
not to work on the local machine because of this.
