# Provision security group ------------------------------

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

# TODO allow HTTPS too?
resource "aws_security_group_rule" "allow_http_inbound_on_app_port" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = var.application_port
  to_port           = var.application_port
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow all HTTP traffic on the app port, ${var.application_port}"
}

# TODO restrict to my IP only?
resource "aws_security_group_rule" "allow_ssh" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 22
  to_port           = 22
  security_group_id = aws_security_group.app_security_group.id
  description = "Allow SSH connection"
}

# Create IAM role for instance ------------------------------

data aws_iam_policy_document "iam_instance_policy_data" {
  statement { # Access to the book images S3 bucket objects
    sid: "AccessToBucketObjects"
    effect = "Allow"
    resources = [
      var.bucket_arn,
      "${var.bucket_arn}/*"
    ]
    actions = [
      "s3:ListBucket",
      "s3:*Object"
    ]
  }
  statement { # Access to the book data DynamoDB table items
    sid = "AccessToTableItems"
    effect = "Allow"
    resources = [ var.table_arn ]
    actions = [
      "dynamodb:DescribeTable",
      "dynamodb:Query",
      "dynamodb:Scan",
      "dynamodb:*Item"
    ]
  }
}

resource "aws_iam_policy" "iam_instance_policy" {
  policy = data.aws_iam_policy_document.iam_instance_assume_role_policy_data.json
}

data aws_iam_policy_document "iam_instance_assume_role_policy_data" {
  statement {
    sid = "AllowAssumeRoleForEc2"
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    },
    actions = [ "sts:AssumeRole" ]
  }
}

# Create role and attach both policies
resource aws_iam_role "iam_instance_role" {
  name = "Role-${var.application_name}-${var.aws_availability_zone}-${var.environment}"
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
  name = "Pr-${var.application_name}-${var.aws_availability_zone}-${var.environment}"
  role = aws_iam_role.iam_instance_role.name
}

# Create key pair which can be used to SSH to instance

resource "tls_private_key" "private_key" {
  algorithm = "rsa"
  rsa_bits = 4096
}

# public key will be uploaded with instance
resource "aws_key_pair" "instance_key_pair" {
  key_name = "Key-${var.application_name}-${var.aws_availability_zone}-${var.environment}"
  public_key = tls_private_key.private_key.public_key_pem
}

# private key is written to file so that it can be added to GitLab artifact outputs
resource "local_sensitive_file" "instance_private_key_file" {
  filename = "ssh_keys/sample-app-private-key-rsa"
  content = tls_private_key.private_key.private_key_pem
}

# Provision Ec2 instance ------------------------------

resource "aws_network_interface" "network_interface" {
  subnet_id = var.subnet_id
  security_groups = [
    aws_security_group.app_security_group.id
  ]
}

resource "aws_instance" "instance" {
  name = "${var.application_name}-${var.aws_availability_zone}-${var.environment}"
  availability_zone = var.aws_availability_zone
  ami = var.ami_id
  instance_type = var.instance_type
  # subnet + security groups is attached to the network interface
  network_interface {
    device_index         = 0
    network_interface_id = aws_network_interface.network_interface.id
  }
  # pass previously created (public) key
  key_name = aws_key_pair.instance_key_pair.key_name
  # to make it work, Terraform user requires "iam:PassRole" permission
  iam_instance_profile = aws_iam_instance_profile.iam_instance_profile.name
  # TODO add permission to access artifacts bucket
  # TODO add parameter for artifact name
  user_data = <<-EOF
  #!/bin/bash
  sudo apt update -y
  sudo amazon-linux-extras install java-openjdk11 -y
  mkdir -p server
  cd server
  aws s3 cp s3://tamas-gaspar-epam-cloudx-artifacts/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
  java -Dspring.profiles.active=${var.environment} -jar demo-0.0.1-SNAPSHOT.jar
  EOF
  user_data_replace_on_change = true # Any change in user_data will recreate instance
}

resource "aws_eip" "elastic_ip" {
  instance = aws_instance.instance.id
  vpc = true
}