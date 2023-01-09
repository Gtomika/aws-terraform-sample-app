# Provision application load balancer

resource "aws_security_group" "load_balancer_security_group" {
  name = "Alb-Sg-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = var.vpc_id
}

resource "aws_security_group_rule" "allow_all_outbound" {
  type              = "egress"
  protocol          = "all"
  from_port         = 0
  to_port           = 65535
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.load_balancer_security_group.id
  description = "Allow all outbound traffic from the load balancer"
}

resource "aws_security_group_rule" "allow_http_inbound_on_app_port" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = var.http_default_port
  to_port           = var.http_default_port
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.load_balancer_security_group.id
  description = "Allow all HTTP traffic through the load balancer"
}

resource "aws_alb" "app_load_balancer" {
  name = "Alb-${var.application_name}-${var.aws_region}-${var.environment}"
  load_balancer_type = "application"

  security_groups = [aws_security_group.load_balancer_security_group.id]
  subnets = [var.subnet_id]

  internal = false
  enable_deletion_protection = false
}

# Target group will forward request to EC2 instances on the app port
# the ASG will add instances into this target group (see module 'auto_scaling_group')
resource "aws_alb_target_group" "app_alb_target_group" {
  name = "Tg-${var.application_name}-${var.aws_region}-${var.environment}"
  port     = var.application_port
  protocol = "HTTP"
  target_type = "instance" # will target the EC2 instances in the autoscaling group
  vpc_id   = var.vpc_id
  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    interval = 30
    protocol = "HTTP"
    port = var.application_port
    path = var.health_check_path
  }
}

# Load balancer will listen on HTTP:80 and forward request to target group
resource "aws_alb_listener" "app_alb_listener" {
  load_balancer_arn = aws_alb.app_load_balancer.arn
  port = var.http_default_port
  protocol = "HTTP"
  default_action {
    type = "forward"
    target_group_arn = aws_alb_target_group.app_alb_target_group.arn
  }
}
