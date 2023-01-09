output "alb_target_group_arn" {
  value = aws_alb_target_group.app_alb_target_group.arn
}

output "alb_public_dns" {
  value = aws_alb.app_load_balancer.dns_name
}