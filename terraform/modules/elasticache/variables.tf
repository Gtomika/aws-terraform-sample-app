variable "aws_region" {
  type = string
}

variable "environment" {
  type = string
}

variable "application_name" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "cache_cluster_nodes" {
  type = number
}

variable "cache_cluster_type" {
  type = string
}

variable "cache_cluster_engine" {
  type = string
  default = "memcached" # app only supports memcached, do not change
}

variable "cache_cluster_port" {
  type = number
  default = 11211
}

variable "cache_cluster_parameter_group" {
  type = string
  default = "default.memcached1.6"
}