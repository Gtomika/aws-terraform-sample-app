## AWS + Terraform sample application

Simple Book API that uses several AWS components:

 - EC2 instance to run the app.
 - DynamoDB to store book data.
 - S3 bucket to store book cover images.
 - SQS queue to send book event notifications.
 - VPN + subnet + elastic IP.

All of these resources are provisioned using Terraform, which is applied through the 
GitLab CI/CD pipeline.
