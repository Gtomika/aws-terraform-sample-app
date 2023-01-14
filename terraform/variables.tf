variable "aws_region" {
  type = string
  description = "AWS region in which the sample app is deployed"
}

variable "aws_availability_zones" {
  type = list(string)
  description = "AWS AZs in which instances are deployed (all must be in 'aws_region')"
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

variable "my_ip" {
  type = string
  description = "My IPv4 address, used to limit SSH to this address"
  sensitive = true
}

variable "asg_min_instances" {
  type = number
  description = "Minimum amount of instances in autoscaling group"
  validation {
    condition = var.asg_min_instances >= 0
    error_message = "Minimum amount must not be negative"
  }
}

variable "asg_max_instances" {
  type = number
  description = "Maximum amount of instances in autoscaling group"
  validation {
    condition = var.asg_max_instances >= 0
    error_message = "Maximum amount must not be negative"
  }
}

variable "asg_desired_capacity" {
  type = number
  description = "Amount of desired instances in autoscaling group"
}

variable "health_check_path" {
  type = string
  description = "Path to which the health check HTTP requests should be sent to"
}

variable "scale_up_at_cpu_usage" {
  type = number
  description = "Percentage of CPU usage when autoscaling group should scale up"
  validation {
    condition = var.scale_up_at_cpu_usage > 0 && var.scale_up_at_cpu_usage < 100
    error_message = "Must be 1-99"
  }
}

variable "scale_down_at_cpu_usage" {
  type = number
  description = "Percentage of CPU usage when autoscaling group should scale down"
  validation {
    condition = var.scale_down_at_cpu_usage > 0 && var.scale_down_at_cpu_usage < 100
    error_message = "Must be 1-99"
  }
}

variable "cached_book_ttl" {
  type = number
  default = "Amount of seconds for how long the ElastiCache should cache books"
  validation {
    condition = var.cached_book_ttl > 0
    error_message = "Cache TTL must be positive"
  }
}

variable "cache_cluster_nodes" {
  type = number
  default = "Amount of nodes (instances) to run in the ElastiCache cluster"
  validation {
    condition = var.cache_cluster_nodes > 0
    error_message = "Cluster node count must be positive"
  }
}

variable "cache_cluster_type" {
  type = string
  description = "Instance type to use for ElastiCache cluster nodes"
}