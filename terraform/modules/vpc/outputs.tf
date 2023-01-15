output "vpc_id" {
  value = aws_vpc.app_vpc.id
}

output public_subnet_ids {
  value = [for public_subnet in aws_subnet.public_subnets: public_subnet.id]
}

output "private_subnet_ids" {
  value = [for private_subnet in aws_subnet.private_subnets: private_subnet.id]
}

output "internal_security_group_id" {
  value = aws_security_group.internal_security_group.id
}