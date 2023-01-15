# Provision security group ------------------------------
resource "aws_security_group" "app_security_group" {
  name = "App-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = var.vpc_id
  description = "Security group for the ${var.application_name} instances"
}

resource "aws_security_group_rule" "allow_all_outbound" {
  type              = "egress"
  protocol          = "all"
  from_port         = 0
  to_port           = 65535
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow all outbound traffic from the instance"
}

resource "aws_security_group_rule" "allow_http_inbound_on_app_port" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = var.application_port
  to_port           = var.application_port
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow all HTTP traffic on the app port, ${var.application_port}"
}

# Create IAM role for instance ------------------------------
data aws_iam_policy_document "iam_instance_policy_data" {
  statement { # Access to the book images S3 bucket objects
    sid = "FullAccessToBucketObjects"
    effect = "Allow"
    resources = [
      var.images_bucket_arn,
      "${var.images_bucket_arn}/*"
    ]
    actions = [
      "s3:ListBucket",
      "s3:*Object*"
    ]
  }
  statement { # Access to the book data DynamoDB table items
    sid = "FullAccessToTableItems"
    effect = "Allow"
    resources = [ var.data_table_arn ]
    actions = [
      "dynamodb:DescribeTable",
      "dynamodb:Query",
      "dynamodb:Scan",
      "dynamodb:*Item*"
    ]
  }
  statement { # to be able to download app JAR
    sid = "AllowGetFromArtifactsBucket"
    effect = "Allow"
    resources = [
      "arn:aws:s3:::${var.artifacts_bucket_name}",
      "arn:aws:s3:::${var.artifacts_bucket_name}/*"
    ]
    actions = [
      "s3:ListBucket",
      "s3:GetObject"
    ]
  }
}

resource "aws_iam_policy" "iam_instance_policy" {
  policy = data.aws_iam_policy_document.iam_instance_policy_data.json
}

data aws_iam_policy_document "iam_instance_assume_role_policy_data" {
  statement {
    sid = "AllowAssumeRoleForEc2"
    effect = "Allow"
    actions = [ "sts:AssumeRole" ]
    principals {
      type        = "Service"
      identifiers = [ "ec2.amazonaws.com" ]
    }
  }
}

# Create role and attach both policies
resource aws_iam_role "iam_instance_role" {
  name = "Role-${var.application_name}-${var.aws_region}-${var.environment}"
  # Here attaches the assume role policy (required for every role)
  assume_role_policy = data.aws_iam_policy_document.iam_instance_assume_role_policy_data.json
}

# here attaches the policy which allows access to S3 and DynamoDB
resource "aws_iam_role_policy_attachment" "attach_policy_to_role" {
  role       = aws_iam_role.iam_instance_role.name
  policy_arn = aws_iam_policy.iam_instance_policy.arn
}

# specify instance profile which is used to attach this role to the EC2 instance
resource "aws_iam_instance_profile" "iam_instance_profile" {
  name = "Pr-${var.application_name}-${var.aws_region}-${var.environment}"
  role = aws_iam_role.iam_instance_role.name
}

# Describe Ec2 instance launch template
resource "aws_launch_template" "app_launch_template" {
  name = "Tmp-${var.application_name}-${var.aws_region}-${var.environment}"
  description = "Launch template for ${var.application_name} app instances, environment: ${var.environment}"

  image_id = var.ami_id
  instance_type = var.instance_type

  # to make it work, Terraform user requires "iam:PassRole" permission
  iam_instance_profile {
     name = aws_iam_instance_profile.iam_instance_profile.name
  }

  vpc_security_group_ids = [
    aws_security_group.app_security_group.id,
    var.internal_security_group_id
  ]

  user_data = base64encode(templatefile(
    "${path.module}/init_script.sh.tftpl",
    local.init_script_config_map
  ))

  disable_api_termination = false
  disable_api_stop = false

  metadata_options {
    http_endpoint = "enabled"
    instance_metadata_tags = "enabled"
  }
}