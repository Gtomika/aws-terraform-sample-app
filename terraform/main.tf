terraform {
    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "~> 4.16"
        }
    }
}

provider "aws" {
    region = var.aws_region
    access_key = var.aws_access_key
    secret_key = var.aws_secret_key
    default_tags {
        tags = {
            application = var.application_name
            managed_by = "terraform"
            environment = var.environment
        }
    }
}

# Build network
module "vpc" {
    source = "modules/vpc"
    aws_region = var.aws_region
    aws_availability_zone = var.aws_availability_zone
    application_name = var.application_name
    environment = var.environment
}

# S3 bucket for book images
module "s3" {
    source = "modules/s3"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    bucket_name = var.book_images_bucket_name,
    aws_account_id = var.aws_account_id
}

# DynamoDB table for book data
module "dynamodb" {
    source = "modules/dynamodb"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    table_name = var.book_data_table_name,
    hash_key_name = var.book_data_table_key
}

# EC2 instance + security group
module "ec2" {
    source = "modules/ec2"
    aws_region = var.aws_region
    aws_availability_zone = var.aws_availability_zone
    application_name = var.application_name
    environment = var.environment
    application_port = var.application_port
    instance_type = var.ec2_instance_type
    ami_id = var.ec2_ami
    vpc_id = module.vpc.vpc_id
    subnet_id = module.vpc.subnet_id
    # added to project GitLab project variables: 'TF_VAR_private_key_path'
    private_key_path = var.private_key_path
    bucket_arn = module.s3.bucket_arn
    table_arn = module.dynamodb.table_arn
}