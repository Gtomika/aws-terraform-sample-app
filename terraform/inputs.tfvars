# Sensitive data such as AWS credentials are
# not here but GitLab projects secrets.

# AWS
aws_region = "eu-central-1"
aws_availability_zones = [
  "eu-central-1a",
  "eu-central-1b"
]

# App
application_name = "CloudxAwsApp"
application_port = 8080

# EC2
ec2_ami = "ami-0a261c0e5f51090b1" # Amazon Linux 2 AMI, 64-bit (x86)
ec2_instance_type = "t2.micro"

# S3
book_images_bucket_name = "tamas-gaspar-cloudx-book-images"
book_icons_bucket_name = "tamas-gaspar-cloudx-book-icons"

# DynamoDB
book_data_table_name = "TamasGasparCloudxBookData"
book_data_table_key = "isbn"
customer_data_table_name="TamasGasparCloudxCustomers"
customer_data_table_key="id"

# Lambda source bucket and key come from Ci/Cd pipeline

# Autoscaling group
asg_min_instances = 1
asg_max_instances = 2
asg_desired_capacity = 1
health_check_path = "/actuator/health"
scale_up_at_cpu_usage = 70 #%
scale_down_at_cpu_usage = 30 #%

# Elasticache
cache_cluster_nodes = 1 #should equal to amount of AZs for best performance
cache_cluster_type = "cache.t2.micro"
cached_book_ttl = 60 #seconds