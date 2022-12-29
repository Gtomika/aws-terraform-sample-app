variable "aws_region" {
  type = string
}

variable "aws_access_key" {
  type = string
  sensitive = true
}

variable "aws_secret_key" {
  type = string
  sensitive = true
}

variable "aws_account_id" {
  type = number
  sensitive = true
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

variable "ec2_ami" {
  type = string
}

variable "ec2_instance_type" {
  type = string
}

variable "book_images_bucket_name" {
  type = string
}

variable "book_data_table_name" {
  type = string
}

variable "book_data_table_key" {
  type = string
}