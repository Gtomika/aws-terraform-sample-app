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
    application_name = var.application_name
    environment = var.environment
    aws_region = var.aws_region
}

module "s3" {
    source = "modules/s3"
    application_name = var.application_name
    environment = var.environment
    aws_region = var.aws_region
    bucket_name = var.book_images_bucket_name,
    aws_account_id = var.aws_account_id
}

module "dynamodb" {
    source = "modules/dynamodb"
    application_name = var.application_name
    environment = var.environment
    aws_region = var.aws_region
    table_name = var.book_data_table_name,
    hash_key_name = var.book_data_table_key
}

module "ec2" {
    source = "modules/ec2"
    application_name = var.application_name
    environment = var.environment
    aws_region = var.aws_region
    application_port = var.application_port
    instance_type = var.ec2_instance_type
    ami_id = var.ec2_ami
    vpc_id = module.vpc.vpc_id
}