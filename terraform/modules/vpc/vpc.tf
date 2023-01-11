# All network infrastructure is provisioned here
# VPC, subnet, route tables, internet gateway

resource "aws_vpc" "vpc" {
  cidr_block = var.vpc_cidr_block

  # Enabling DNS is required for VPC endpoints to work!
  enable_dns_hostnames = true
  enable_dns_support = true
  tags = {
    Name = "Vpc-${var.application_name}-${var.aws_region}-${var.environment}"
  }
}

# Define a public and private subnet for each AZs
resource "aws_subnet" "public_subnets" {
  count = length(var.public_subnet_cidr_blocks)

  vpc_id = aws_vpc.vpc.id
  cidr_block = element(var.public_subnet_cidr_blocks, count.index)
  availability_zone = element(var.aws_availability_zones, count.index)
  map_public_ip_on_launch = true # Resources in this subnet should have a public IP
  tags = {
    Name = "Pub-${var.application_name}-${element(var.aws_availability_zones, count.index)}-${var.environment}"
  }
}

resource "aws_subnet" "private_subnets" {
  count = length(var.private_subnet_cidr_blocks)

  vpc_id = aws_vpc.vpc.id
  cidr_block = element(var.private_subnet_cidr_blocks, count.index)
  availability_zone = element(var.aws_availability_zones, count.index)
  map_public_ip_on_launch = false
  tags = {
    Name = "Prv-${var.application_name}-${element(var.aws_availability_zones, count.index)}-${var.environment}"
  }
}

resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.vpc.id
}

# Route table of the public subnets
resource "aws_route_table" "public_subnets_route_table" {
  vpc_id = aws_vpc.vpc.id
  # 'local' route is created implicitly
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }
}

resource "aws_route_table_association" "public_subnets_route_table_association" {
  count = length(aws_subnet.public_subnets)

  route_table_id = aws_route_table.public_subnets_route_table.id
  subnet_id = aws_subnet.public_subnets[count.index].id
}

# Route table for the private subnets
resource "aws_route_table" "private_subnets_route_table" {
  vpc_id = aws_vpc.vpc.id
  # 'local' route is created implicitly
}

resource "aws_route_table_association" "private_subnets_route_table_association" {
  count = length(aws_subnet.private_subnets)

  route_table_id = aws_route_table.private_subnets_route_table.id
  subnet_id = aws_subnet.private_subnets[count.index].id
}

# Internal security group. All instances will get this to be able to
# connect to each other regardless of protocol/port
resource "aws_security_group" "internal_security_group" {
  name = "Int-${var.application_name}-${var.aws_region}-${var.environment}"
  description = "${var.application_name} internal security group"
  vpc_id = aws_vpc.vpc.id
}

resource "aws_security_group_rule" "internal_access_rule" {
  type              = "ingress"
  from_port         = 0
  to_port           = 65535
  protocol          = "all"
  security_group_id = aws_security_group.internal_security_group.id
  source_security_group_id = aws_security_group.internal_security_group.id
  description = "Allow all traffic from other resources with this security group"
}

# VPC gateway endpoints for S3 and dynamoDB
# gateway endpoint only supports s3 and dynamodb (how lucky), and is free
resource "aws_vpc_endpoint" "s3_vpc_endpoint" {
  service_name = "com.amazonaws.${var.aws_region}.s3"
  vpc_id       = aws_vpc.vpc.id
  auto_accept = true
  vpc_endpoint_type = "Gateway"
  route_table_ids = [
    aws_route_table.public_subnets_route_table.id,
    aws_route_table.private_subnets_route_table.id,
    aws_vpc.vpc.main_route_table_id
  ]
  tags = {
    Name = "S3EP-${var.application_name}-${var.aws_region}"
  }
}

resource "aws_vpc_endpoint" "dynamodb_vpc_endpoint" {
  service_name = "com.amazonaws.${var.aws_region}.dynamodb"
  vpc_id       = aws_vpc.vpc.id
  auto_accept = true
  vpc_endpoint_type = "Gateway"
  route_table_ids = [
    aws_route_table.public_subnets_route_table.id,
    aws_route_table.private_subnets_route_table.id,
    aws_vpc.vpc.main_route_table_id
  ]
  tags = {
    Name = "DynamoEP-${var.application_name}-${var.aws_region}"
  }
}

# All calls to the public IPs of S3 or dynamodb in this region
# will be directed toward the VPC gateway endpoints instead (route table handles this).
# So, traffic does not leave the AWS cloud, and no code changes are required.



