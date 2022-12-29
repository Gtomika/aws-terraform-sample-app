# Provision security group and EC2 instance

resource "aws_security_group" "app_security_group" {
  name = "Sg-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = var.vpc_id
}

resource "aws_security_group_rule" "allow_all_outbound" {
  type              = "egress"
  protocol          = "all"
  from_port         = 0
  to_port           = 65535
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow all outbound traffic from the instance"
}

resource "aws_security_group_rule" "allow_http_inbound_on_app_port" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = var.application_port
  to_port           = var.application_port
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow all HTTP traffic on the app port"
}

# TODO allow HTTPS too?

