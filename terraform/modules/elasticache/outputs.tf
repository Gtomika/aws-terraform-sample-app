output "cache_cluster_private_dns" {
  value = aws_elasticache_cluster.app_cache_cluster.cluster_address
}

output "cache_cluster_port" {
  value = aws_elasticache_cluster.app_cache_cluster.port
}