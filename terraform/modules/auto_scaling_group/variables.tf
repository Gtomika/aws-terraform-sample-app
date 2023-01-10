variable "aws_region" {
  type = string
}

variable "environment" {
  type = string
}

variable "application_name" {
  type = string
}


variable "app_launch_template_id" {
  type = string
}

variable "asg_min_instances" {
  type = number
}

variable "asg_max_instances" {
  type = number
}

variable "asg_desired_capacity" {
  type = number
}

variable "alb_target_group_arn" {
  type = string
}

variable "scale_up_at_cpu_usage" {
  type = number
}

variable "scale_down_at_cpu_usage" {
  type = number
}

variable "public_subnet_ids" {
  type = list(string)
}