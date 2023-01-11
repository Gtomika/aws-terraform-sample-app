variable "aws_region" {
  type = string
}

variable "environment" {
  type = string
}

variable "application_name" {
  type = string
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

variable "my_ip" {
  type = string
  description = "My IPv4 address, used to limit SSH to this address"
  sensitive = true
}

variable "internal_security_group_id" {
  type = string
}