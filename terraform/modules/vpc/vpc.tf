# All network infrastructure is provisioned here
# VPC, subnet, route tables, internet gateway

resource "aws_vpc" "vpc" {
  cidr_block = var.vpc_cidr_block
  enable_dns_hostnames = true # Instances with public IP will also get DNS names
  tags = {
    Name = "Vpc-${var.application_name}-${var.aws_region}-${var.environment}"
  }
}

# Define a public subnet for each AZs
resource "aws_subnet" "public_subnets" {
  count = length(var.aws_availability_zones)

  vpc_id = aws_vpc.vpc.id
  cidr_block = element(var.public_subnet_cidr_blocks, count.index)
  availability_zone = element(var.aws_availability_zones, count.index)
  map_public_ip_on_launch = true # Instance in this subnet should have a public IP
  tags = {
    Name = "Sub-${var.application_name}-${var.aws_region}-${var.environment}"
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
  count = length(var.aws_availability_zones)

  route_table_id = aws_route_table.public_subnets_route_table.id
  subnet_id = element(aws_subnet.public_subnets, count.index)
}