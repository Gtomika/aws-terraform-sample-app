output "application_name" {
  value = var.application_name
  description = "App name"
}

output "environment" {
  value = var.environment
  description = "App environment"
}

output "application_port" {
  value = var.application_port
  description = "Application port"
}

output "aws_region" {
  value = var.aws_region
  description = "AWS deployment region"
}

output "application_artifact_name" {
  value = var.application_artifact_name
  description = "Artifact of this deployment, which is in S3 bucket 'artifacts_bucket_name'. Use CI/CD to manage!"
}

output "artifacts_bucket_name" {
  value = var.artifacts_bucket_name
  description = "Name of S3 bucket where application artifact is. Use CI/CD to manage!"
}

output "load_balancer_public_url" {
  value = module.application_load_balancer.alb_public_dns
  description = "Load balancer of the application can be found at this URL"
}

output "images_bucket_name" {
  value = module.s3.bucket_name
  description = "Name of the book images S3 bucket"
}

output "data_table_name" {
  value = module.dynamodb.table_name
  description = "Name of the book data DynamoDB table"
}