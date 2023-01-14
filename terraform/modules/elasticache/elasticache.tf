# Provision elasticache cluster

resource "aws_security_group" "cache_cluster_security_group" {
  name = "Cache-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = var.vpc_id
  description = "Security group for ${var.application_name} elasticache cluster"
}

resource "aws_security_group_rule" "allow_inbound_cache_port_rule" {
  type              = "ingress"
  from_port         = var.cache_cluster_port
  to_port           = var.cache_cluster_port
  protocol          = "tcp"
  security_group_id = aws_security_group.cache_cluster_security_group.id
  description = "Allow inbound traffic on cache cluster port ${var.cache_cluster_port}"
}

# place the cache nodes in the private subnets
resource "aws_elasticache_subnet_group" "cache_cluster_subnet_group" {
  name       = "Group-${var.application_name}-${var.aws_region}-${var.environment}"
  subnet_ids = var.private_subnet_ids
}

resource "aws_elasticache_cluster" "app_cache_cluster" {
  cluster_id = "${var.application_name}-${var.aws_region}-${var.environment}"

  engine = var.cache_cluster_engine
  num_cache_nodes = var.cache_cluster_nodes
  node_type = var.cache_cluster_type
  port = var.cache_cluster_port
  parameter_group_name = var.cache_cluster_parameter_group

  subnet_group_name = aws_elasticache_subnet_group.cache_cluster_subnet_group.name
  security_group_ids = [aws_security_group.cache_cluster_security_group.id]
}