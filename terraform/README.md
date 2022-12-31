## Terraform structure

The IaC consists of the root module and submodules, located in the 
``modules`` directory:

 - ``vpc``: Defines network resources for the app.
 - ``s3``: Defines S3 bucket and bucket policies to store book images.
 - ``dynamodb``: Defines DynamoDB table to store book data.
 - ``ec2``: Defines the EC2 instance and related configurations.

## Terraform inputs

The following inputs are required to make this configuration work:

 - ``inputs.auto.tfvars``: Provides the not sensitive info, will be picked 
up by Terraform automatically.
 - ``secrets.tfvars``: Provides the sensitive info such as AWS access keys. 
Not checked into Git, but provided to the pipeline as GitLab project variable.
 - ``environment``: Application environment. For simplicity's sake only ``prod`` 
is used for now.
 - ``artifacts_bucket_name``: Name of S3 bucket where application artifact is.
 - ``application_artifact_name``: This is the key of the packages app JAR in the artifacts 
bucket. There is a pipeline job which uploads the artifact to S3 before terraform runs. 

## Terraform user and permissions

Terraform scripts are using the credentials of the AWS IAM user ``terraform``. These 
are in the ``secrets.tfvars`` which is not version controlled.  The Terraform user 
has only the necessary permissions to be able to apply this configuration.

All the IAM policies that Terraform needs are in the ``iam_policies`` directory.

## Terraform state file

The state file is stored in an S3 bucket. Terraform must have the required permissions 
on this bucket, and it must exist before running Terraform scripts.

**TODO:** try to set up GitLab managed terraform state?
