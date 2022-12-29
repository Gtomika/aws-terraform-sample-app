# All network infrastructure is provisioned here
# VPC, subnet, route tables, internet gateway

resource "aws_vpc" "vpc" {
  name = "Vpc-${var.application_name}-${var.aws_region}-${var.environment}"
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "subnet" {
  name = "Sub-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.0.0/24"
}

resource "aws_internet_gateway" "internet_gateway" {
  name = "Igw-${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = aws_vpc.vpc.id
}

# Route table of the subnet
resource "aws_route_table" "route_table" {
  name = "Rt--${var.application_name}-${var.aws_region}-${var.environment}"
  vpc_id = aws_vpc.vpc.id
  # 'local' route is created implicitly
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }
}

resource "aws_route_table_association" "route_table_association" {
  route_table_id = aws_route_table.route_table.id
  subnet_id = aws_subnet.subnet.id
}