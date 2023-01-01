variable "aws_region" {
  type = string
}

variable "aws_availability_zone" {
  type = string
}

variable "environment" {
  type = string
}

variable "application_name" {
  type = string
}

variable "application_port" {
  type = number
}

variable "instance_type" {
  type = string
}

variable "ami_id" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "subnet_id" {
  type = string
}

variable "images_bucket_arn" {
  type = string
}

variable "images_bucket_name" {
  type = string
}

variable "artifacts_bucket_name" {
  type = string
}

variable "data_table_arn" {
  type = string
}

variable "data_table_name" {
  type = string
}

variable "application_artifact_name" {
  type = string
}