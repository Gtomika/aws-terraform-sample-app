# Topic for book notifications

resource "aws_sns_topic" "book_api_notifications_topic" {
  name = "${var.application_name}-${var.aws_region}-${var.environment}"
}