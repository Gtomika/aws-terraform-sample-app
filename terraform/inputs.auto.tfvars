# Sensitive data such as AWS credentials are
# not here but in 'secrets.auto.tfvars' which is not
# checked into git, but uploaded to GitLab as project variable.

# AWS
aws_region = "eu-central-1"
aws_availability_zone = "eu-central-1a"

# App
environment = "prod"
application_name = "EpamCloudxAwsApp"
application_port = 8080

# EC2
ec2_ami = ""
ec2_instance_type = "t2.micro"

# S3
book_images_bucket_name = "epam-cloudx-book-images"

# DynamoDB
book_data_table_name = "EpamCloudxBookData"
book_data_table_key = "isbn"