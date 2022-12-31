terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
  # Using S3 bucket to store terraform state
  backend "s3" { # variables not allowed here :(
    bucket = "tamas-gaspar-epam-cloudx-terraform-state"
    key = "EpamCloudxAwsApp"
    region = "eu-central-1"
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