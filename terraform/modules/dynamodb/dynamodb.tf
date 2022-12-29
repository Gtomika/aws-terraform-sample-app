# Create DynamoDB table for book data

resource "aws_dynamodb_table" "dynamodb_table" {
  name = "${var.table_name}-${var.aws_region}-${var.environment}"
  billing_mode   = "PROVISIONED"
  read_capacity = 5
  write_capacity = 5
  hash_key = var.hash_key_name
  attribute {
    name = var.hash_key_name
    type = "S"
  }
}