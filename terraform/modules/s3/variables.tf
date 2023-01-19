variable "aws_region" {
  type = string
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

variable "book_images_bucket_name" {
  type = string
}

variable "book_icons_bucket_name" {
  type = string
}
