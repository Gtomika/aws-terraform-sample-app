# Provision bastion instance in public subnet.
# Accepts SSH from only my IP.

# Build security group
resource "aws_security_group" "bastion_security_group" {
  vpc_id = var.vpc_id
  name = "Bsg-${var.application_name}-${var.aws_region}-${var.environment}"
  description = "Security for bastion host"
}

resource "aws_security_group_rule" "allow_all_outbound" {
  type              = "egress"
  protocol          = "all"
  from_port         = 0
  to_port           = 65535
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.bastion_security_group.id
  description = "Allow all outbound traffic from the instance"
}

resource "aws_security_group_rule" "allow_ssh" {
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 22
  to_port           = 22
  cidr_blocks       = ["${var.my_ip}/32"]
  security_group_id = aws_security_group.bastion_security_group.id
  description = "Allow SSH connection from my IP only"
}

# Create key pair which can be used to SSH to instance
resource "tls_private_key" "private_key" {
  algorithm = "RSA"
  rsa_bits = 4096
}

# public key will be uploaded with instance
resource "aws_key_pair" "instance_key_pair" {
  key_name = "Key-${var.application_name}-${var.aws_region}-${var.environment}"
  public_key = tls_private_key.private_key.public_key_openssh
}

# private key is written to file so that it can be added to GitLab artifact outputs
resource "local_sensitive_file" "instance_private_key_file" {
  filename = "${path.root}/ssh_keys/sample-app-private-key-rsa.pem"
  content = tls_private_key.private_key.private_key_pem
}

resource "aws_instance" "bastion_instance" {
  instance_type = "t2.nano" # fixed instance type for bastion
  ami = var.ami_id
  subnet_id = var.public_subnet_ids[0]

  # pass previously created (public) key
  key_name = aws_key_pair.instance_key_pair.key_name

  vpc_security_group_ids = [
    aws_security_group.bastion_security_group.id,
    var.internal_security_group_id
  ]

  disable_api_termination = false
  disable_api_stop = false

  tags = {
    Name = "Bastion-${var.application_name}-${var.aws_region}-${var.environment}"
  }
}