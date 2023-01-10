variable "aws_region" {
  type = string
}

variable "aws_availability_zones" {
  type = list(string)
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

variable "public_subnet_ids" {
  type = list(string)
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

variable "my_ip" {
  type = string
  description = "My IPv4 address, used to limit SSH to this address"
  sensitive = true
}