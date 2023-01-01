terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
  # Using S3 bucket to store terraform state
  # set in 'backend.hcl'
  backend "s3" {}
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