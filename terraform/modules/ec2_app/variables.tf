variable "aws_region" {
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

variable "images_bucket" {
  type = object({
    bucket = string
    arn = string
  })
}

variable "artifacts_bucket_name" {
  type = string
}

variable "book_data_table" {
  type = object({
    name = string
    arn = string
  })
}

variable "customer_data_table" {
  type = object({
    name = string
    arn = string
  })
}

variable "application_artifact_name" {
  type = string
}

variable "internal_security_group_id" {
  type = string
}

variable "cache_cluster_private_dns" {
  type = string
}

variable "cache_cluster_port" {
  type = number
}

variable "book_cache_ttl" {
  type = number
}

variable "book_api_notification_topic_arn" {
  type = string
}

locals {
  # in this map all variables used inside the init script template must be given a value
  # 'init_script.sh.tftpl'
  init_script_config_map = {
    artifacts_bucket_name = var.artifacts_bucket_name
    application_artifact_name = var.application_artifact_name
    environment = var.environment
    application_port = var.application_port
    aws_region = var.aws_region
    book_data_table_name = var.book_data_table.name
    customer_data_table_name = var.customer_data_table.name
    images_bucket_name = var.images_bucket.bucket
    cache_cluster_private_dns = var.cache_cluster_private_dns
    cache_cluster_port = var.cache_cluster_port
    book_cache_ttl = var.book_cache_ttl
    book_api_notification_topic_arn = var.book_api_notification_topic_arn
  }
}