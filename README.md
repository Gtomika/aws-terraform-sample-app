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

## Possible improvements

A list of improvements that I'm aware of, but had no time yet to implement 
them:

 - Provision load balancer and auto-scaling group instead of just 
plain EC2 instance.
 - Use VPC endpoints to reach S3 and DynamoDB instead of connecting
through the public internet.
 - Set up staging environment, only having ``prod`` is not good practice.
 - Configure logging to send logs to some observability platform (find free 
alternative). Right now it is logging to local file and console.



