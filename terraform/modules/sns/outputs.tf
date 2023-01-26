output "book_api_topic_name" {
  value = aws_sns_topic.book_api_notifications_topic.name
}

output "book_api_topic_arn" {
  value = aws_sns_topic.book_api_notifications_topic.arn
}