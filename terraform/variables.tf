variable "aws_region" {
  type = string
  description = "AWS region in which the sample app is deployed"
}

variable "aws_availability_zone" {
  type = string
  description = "AWS AZ which must be in the 'aws_region'"
}

variable "aws_access_key" {
  type = string
  sensitive = true
  description = "AWS API access key for 'terraform' user"
}

variable "aws_secret_key" {
  type = string
  sensitive = true
  description = "AWS API secret key for 'terraform' user"
}

variable "aws_account_id" {
  type = number
  sensitive = true
  description = "8 digit account ID of my personal AWS account"
}

variable "environment" {
  type = string
  description = "Environment in which app is deployed, for now there is only one value: 'prod'"
}

variable "application_name" {
  type = string
  description = "App name used to name resources"
}

variable "application_port" {
  type = number
  description = "Port which app is using, used in security group rules"
}

variable "application_artifact_name" {
  type = string
  description = "Name of JAR file which the EC2 instance must pull from S3"
}

variable "ec2_ami" {
  type = string
  description = "EC2 instances AMI ID"
  validation {
    condition     = length(var.ec2_ami) > 4 && substr(var.ec2_ami, 0, 4) == "ami-"
    error_message = "The image_id value must be a valid AMI id, starting with \"ami-\"."
  }
}

variable "ec2_instance_type" {
  type = string
  description = "EC2 instance type. Terraform user is only allowed to start 't2.micro' instance"
}

variable "book_images_bucket_name" {
  type = string
  description = "Name of S3 bucket in which book images is stored"
}

variable "artifacts_bucket_name" {
  type = string
  description = "Name of S3 bucket in which packages JARs of the app are stored. Used in instance init script"
}

variable "book_data_table_name" {
  type = string
  description = "DynamoDB table name of book data table"
}

variable "book_data_table_key" {
  type = string
  description = "Hash key of the 'book_data_table_name' table"
}