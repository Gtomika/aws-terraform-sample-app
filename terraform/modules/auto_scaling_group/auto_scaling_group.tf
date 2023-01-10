resource "aws_autoscaling_group" "app_autoscaling_group" {
  name = "Asg-${var.application_name}-${var.aws_region}-${var.environment}"
  min_size = var.asg_min_instances
  max_size = var.asg_max_instances
  desired_capacity = var.asg_desired_capacity
  # Determines which subnets to launch the instances in
  vpc_zone_identifier = var.public_subnet_ids

  launch_template {
    id = var.app_launch_template_id
    version = "$Latest"
  }

  health_check_type = "ELB"

  # Here it is said to attach all instances in this ASG to target group of load balancer
  target_group_arns = [
    var.alb_target_group_arn
  ]
}

resource "aws_autoscaling_policy" "app_scale_up_policy" {
  name = "ScaleUp-${var.application_name}-${var.aws_region}-${var.environment}"
  scaling_adjustment = 1
  adjustment_type = "ChangeInCapacity"
  cooldown = 300
  autoscaling_group_name = aws_autoscaling_group.app_autoscaling_group.name
}

# This alarm triggers at high CPU usage and instructs the autoscaling group to scale up
resource "aws_cloudwatch_metric_alarm" "app_cpu_usage_high_alarm" {
  alarm_name = "CpuHighAlarm-${var.application_name}-${var.aws_region}-${var.environment}"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods = "2"
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = "120"
  statistic = "Average"
  threshold = var.scale_up_at_cpu_usage
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.app_autoscaling_group.name
  }
  alarm_description = "This metric monitor EC2 instance CPU utilization"
  alarm_actions = [ aws_autoscaling_policy.app_scale_up_policy.arn ]
}

resource "aws_autoscaling_policy" "app_scale_down_policy" {
  name = "ScaleDown-${var.application_name}-${var.aws_region}-${var.environment}"
  scaling_adjustment = -1
  adjustment_type = "ChangeInCapacity"
  cooldown = 300
  autoscaling_group_name =  aws_autoscaling_group.app_autoscaling_group.name
}

# This alarm triggers at low CPU usage and instructs the autoscaling group to scale down
resource "aws_cloudwatch_metric_alarm" "app_cpu_usage_low_alarm" {
  alarm_name = "CpuLowAlarm-${var.application_name}-${var.aws_region}-${var.environment}"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods = "2"
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = "120"
  statistic = "Average"
  threshold = var.scale_down_at_cpu_usage
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.app_autoscaling_group.name
  }
  alarm_description = "This metric monitor EC2 instance CPU utilization"
  alarm_actions = [ aws_autoscaling_policy.app_scale_down_policy.arn ]
}