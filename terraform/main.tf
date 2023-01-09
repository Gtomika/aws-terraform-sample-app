# Build network
module "vpc" {
    source = "./modules/vpc"
    aws_region = var.aws_region
    aws_availability_zone = var.aws_availability_zone
    application_name = var.application_name
    environment = var.environment
}

# S3 bucket for book images
module "s3" {
    source = "./modules/s3"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    bucket_name = var.book_images_bucket_name
    aws_account_id = var.aws_account_id
}

# DynamoDB table for book data
module "dynamodb" {
    source = "./modules/dynamodb"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    table_name = var.book_data_table_name
    hash_key_name = var.book_data_table_key
}

# EC2 launch template (+security group, +IAM policies for it)
module "ec2" {
    source = "./modules/ec2"
    aws_region = var.aws_region
    aws_availability_zone = var.aws_availability_zone
    application_name = var.application_name
    application_port = var.application_port
    application_artifact_name = var.application_artifact_name
    environment = var.environment
    instance_type = var.ec2_instance_type
    ami_id = var.ec2_ami
    vpc_id = module.vpc.vpc_id
    subnet_id = module.vpc.subnet_id
    images_bucket_arn = module.s3.bucket_arn
    images_bucket_name = module.s3.bucket_name
    artifacts_bucket_name = var.artifacts_bucket_name
    data_table_arn = module.dynamodb.table_arn
    data_table_name = module.dynamodb.table_name
    my_ip = var.my_ip
}

# Set up application load balancer
module "application_load_balancer" {
    source = "./modules/application_load_balancer"
    aws_region = var.aws_region
    application_name = var.application_name
    application_port = var.application_port
    environment = var.environment
    aws_availability_zone = var.aws_availability_zone
    vpc_id = module.vpc.vpc_id
    subnet_id = module.vpc.subnet_id
    health_check_path = var.health_check_path
}

# Create auto scaling group
module "auto_scaling_group" {
    source = "./modules/auto_scaling_group"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    aws_availability_zone = var.aws_availability_zone
    app_launch_template_id = module.ec2.app_launch_template_id
    asg_min_instances = var.asg_min_instances
    asg_max_instances = var.asg_max_instances
    asg_desired_capacity = var.asg_desired_capacity
    alb_target_group_arn = module.application_load_balancer.alb_target_group_arn
    scale_up_at_cpu_usage = var.scale_up_at_cpu_usage
    scale_down_at_cpu_usage = var.scale_down_at_cpu_usage
}