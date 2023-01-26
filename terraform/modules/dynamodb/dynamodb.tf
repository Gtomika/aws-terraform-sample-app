# Create DynamoDB tables for book data and customers

resource "aws_dynamodb_table" "book_data_table" {
  name = "${var.book_data_table_name}-${var.aws_region}-${var.environment}"
  billing_mode   = "PROVISIONED"
  read_capacity = 5
  write_capacity = 5
  hash_key = var.book_data_hash_key_name
  attribute {
    name = var.book_data_hash_key_name
    type = "S" # string
  }
  point_in_time_recovery {
    enabled = false
  }
  stream_enabled = false
}

resource "aws_dynamodb_table" "customers_data_table" {
  name = "${var.customer_data_table_name}-${var.aws_region}-${var.environment}"
  billing_mode   = "PROVISIONED"
  read_capacity = 5
  write_capacity = 5
  hash_key = var.customer_data_hash_key_name
  attribute {
    name = var.customer_data_hash_key_name
    type = "S"
  }
  point_in_time_recovery {
    enabled = false
  }
  stream_enabled = false
}