output "instance_public_url" {
  value = aws_instance.instance.public_dns
}

output "instance_key_name" {
  value = aws_instance.instance.key_name
}