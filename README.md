## AWS + Terraform sample application

Simple Book API that uses several AWS components:

 - EC2 instance to run the app.
 - DynamoDB table to store book data.
 - S3 bucket to store book cover images (included bucket policy).
 - VPN, subnet, internet gateway, elastic IP, security group.
 - IAM role and policy with the principle of the least privilege.

All of these resources are provisioned using Terraform, which is applied through the 
GitLab CI/CD pipeline.

See the README-s in the folders for more information.



