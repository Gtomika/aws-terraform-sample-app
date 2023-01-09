output "app_launch_template_id" {
  value = aws_launch_template.app_launch_template.id
}

output "instance_key_name" {
  value = aws_launch_template.app_launch_template.key_name
}