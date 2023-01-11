output "bastion_host_url" {
  value = aws_instance.bastion_instance.public_dns
}