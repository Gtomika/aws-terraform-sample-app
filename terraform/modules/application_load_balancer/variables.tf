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

variable "vpc_id" {
  type = string
}

variable "subnet_id" {
  type = string
}

variable "health_check_path" {
  type = string
}

variable "http_default_port" {
  type = number
  default = 80
}