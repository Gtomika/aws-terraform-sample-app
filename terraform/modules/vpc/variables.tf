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

variable "vpc_cidr_block" {
  type = string
  default = "10.0.0.0/16"
}

variable "private_subnet_cidr_blocks" {
  type = list(string)
  default = [
    "10.0.3.0/24",
    "10.0.4.0/24"
  ],
  description = "CIDR blocks to use for private subnets, Must have same length as 'aws_availability_zones'"
}

variable "public_subnet_cidr_blocks" {
  type = list(string)
  default = [
    "10.0.5.0/24",
    "10.0.6.0/24"
  ]
  description = "CIDR blocks to use for public subnets, Must have same length as 'aws_availability_zones'"
}
