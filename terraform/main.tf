# Build network
module "vpc" {
    source = "./modules/vpc"
    aws_region = var.aws_region
    aws_availability_zones = var.aws_availability_zones
    application_name = var.application_name
    environment = var.environment
}

# S3 bucket for book images and icons
module "s3" {
    source = "./modules/s3"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    book_images_bucket_name = var.book_images_bucket_name
    book_icons_bucket_name = var.book_icons_bucket_name
    aws_account_id = var.aws_account_id
}

# DynamoDB table for book data and customers
module "dynamodb" {
    source = "./modules/dynamodb"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    book_data_table_name = var.book_data_table_name
    book_data_hash_key_name = var.book_data_table_key
    customer_data_table_name = var.customer_data_table_name
    customer_data_hash_key_name = var.customer_data_table_key
}

# Caching layer for the app
module "elasticache" {
    source = "./modules/elasticache"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    cache_cluster_nodes = var.cache_cluster_nodes
    cache_cluster_type = var.cache_cluster_type

    vpc_id = module.vpc.vpc_id
    private_subnet_ids = module.vpc.private_subnet_ids
}

module "sns" {
    source = "./modules/sns"
    application_name = var.application_name
    aws_region = var.aws_region
    environment = var.environment
}

# bastion host
module "bastion_ec2" {
    source = "./modules/ec2_bastion"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    ami_id = var.ec2_ami
    my_ip = var.my_ip

    vpc_id = module.vpc.vpc_id
    public_subnet_ids = module.vpc.public_subnet_ids
    internal_security_group_id = module.vpc.internal_security_group_id
}

# app EC2 launch template (+security group, +IAM policies for it)
module "app_ec2" {
    source = "./modules/ec2_app"
    aws_region = var.aws_region
    application_name = var.application_name
    application_port = var.application_port
    application_artifact_name = var.application_artifact_name
    environment = var.environment
    instance_type = var.ec2_instance_type
    ami_id = var.ec2_ami
    book_cache_ttl = var.cached_book_ttl

    vpc_id = module.vpc.vpc_id
    images_bucket = module.s3.images_bucket
    artifacts_bucket_name = var.artifacts_bucket_name
    book_data_table = module.dynamodb.book_data_table
    customer_data_table = module.dynamodb.customer_data_table
    internal_security_group_id = module.vpc.internal_security_group_id
    cache_cluster_private_dns = module.elasticache.cache_cluster_private_dns
    cache_cluster_port = module.elasticache.cache_cluster_port
    book_api_notification_topic_arn = module.sns.book_api_topic_arn
}

# Set up application load balancer
module "application_load_balancer" {
    source = "./modules/application_load_balancer"
    aws_region = var.aws_region
    application_name = var.application_name
    application_port = var.application_port
    environment = var.environment
    health_check_path = var.health_check_path

    vpc_id = module.vpc.vpc_id
    public_subnet_ids = module.vpc.public_subnet_ids
}

# Create auto scaling group
module "auto_scaling_group" {
    source = "./modules/auto_scaling_group"
    aws_region = var.aws_region
    application_name = var.application_name
    environment = var.environment
    asg_min_instances = var.asg_min_instances
    asg_max_instances = var.asg_max_instances
    asg_desired_capacity = var.asg_desired_capacity
    scale_up_at_cpu_usage = var.scale_up_at_cpu_usage
    scale_down_at_cpu_usage = var.scale_down_at_cpu_usage

    app_launch_template_id = module.app_ec2.app_launch_template_id
    alb_target_group_arn = module.application_load_balancer.alb_target_group_arn
    private_subnet_ids = module.vpc.private_subnet_ids
}