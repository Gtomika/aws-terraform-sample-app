## Terraform structure

The IaC consists of the root module and submodules, located in the 
``modules`` directory:

 - ``vpc``: Defines network resources for the app.
 - ``s3``: Defines S3 bucket and bucket policies to store book images.
 - ``dynamodb``: Defines DynamoDB table to store book data.
 - ``ec2``: Defines the EC2 instance and related configurations. The result will be a launch template.
 - ``application_load_balancer``: Load balancer with target group and listener configs.
 - ``auto_scaling_group``: ASG with CloudWatch alarms that trigger the scaling.

I didn't use any pre-written Terraform modules because the goal of this project is to learn the basics.

## Terraform inputs

The following inputs are required to make this configuration work:

 - ``inputs.tfvars``: Provides the not sensitive info in bulk.
 - ``environment``: Application environment. For simplicity's sake only ``prod`` 
is used for now.
 - ``aws_access_key``: Access key of Terraform user.
 - ``aws_secret_key``: Secret key of Terraform user.
 - ``aws_account_id``: 8 digit AWS account ID.
 - ``artifacts_bucket_name``: Name of S3 bucket where application artifact is.
 - ``application_artifact_name``: This is the key of the packages app JAR in the artifacts 
bucket. There is a pipeline job which uploads the artifact to S3 before terraform runs. 

Additionally, the ``backend.hcl`` file also must be provided.

## Terraform user and permissions

Terraform scripts are using the credentials of the AWS IAM user ``terraform``. These 
are provided as GitLab project secrets and are not version controlled. The Terraform user 
has only the necessary permissions to be able to apply this configuration.

All the IAM policies that Terraform needs are in the ``iam_policies`` directory (with sensitive 
info removed).

## Terraform state file

The state file is stored in an S3 bucket. Terraform must have the required permissions 
on this bucket, and it must exist before running Terraform scripts.

**TODO:** try to set up GitLab managed terraform state?
