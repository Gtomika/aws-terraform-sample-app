# Sensitive data such as AWS credentials are
# not here but GitLab projects secrets.

# AWS
aws_region = "eu-central-1"
aws_availability_zone = "eu-central-1a"

# App
application_name = "EpamCloudxAwsApp"
application_port = 8080

# EC2
ec2_ami = "ami-0a261c0e5f51090b1" # Amazon Linux 2 AMI, 64-bit (x86)
ec2_instance_type = "t2.micro"

# S3
book_images_bucket_name = "tamas-gaspar-epam-cloudx-book-images"

# DynamoDB
book_data_table_name = "TamasGasparEpamCloudxBookData"
book_data_table_key = "isbn"