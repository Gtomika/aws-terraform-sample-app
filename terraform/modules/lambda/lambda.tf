## create lambda functions that rescale and delete images

# Define the IAM policy that gives the needed permissions to lambda
data "aws_iam_policy_document" "manage_book_icons_lambda_policy_data" {
  statement {
    sid = "AllowLoggingForLambda"
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
    resources = ["arn:aws:logs:*:*:*"]
  }
  statement {
    sid = "AllowGetObjectFromImagesBucket"
    effect = "Allow"
    actions = ["s3:GetObject"]
    resources = ["${var.book_images_bucket.arn}/*"]
  }
  statement {
    sid = "AllowPutObjectToIconsBucket"
    effect = "Allow"
    actions = ["s3:PutObject"]
    resources = ["${var.book_icons_bucket.arn}/*"]
  }
}

resource "aws_iam_policy" "manage_book_icons_lambda_policy" {
  name = "IconManagerLambdaPolicy-${var.application_name}-${var.environment}"
  policy = data.aws_iam_policy_document.manage_book_icons_lambda_policy_data.json
}

# Allow the lambda to assume the role
data "aws_iam_policy_document" "lambda_assume_role_policy_data" {
  statement {
    sid = "AllowLambdaToAssumeRole"
    effect = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }
    resources = ["*"]
  }
}

resource "aws_iam_role" "manage_book_icons_lambda_role" {
  name = "IconManagerLambdaRole-${var.application_name}-${var.environment}"
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role_policy_data.json
}

# Define lambda
resource "aws_lambda_function" "manage_book_icons_lambda" {
  function_name = "book-icons-manager-${var.application_name}-${var.environment}"
  role          = aws_iam_role.manage_book_icons_lambda_role.arn
  description = "Manages book icons, which are resized versions of book images. Triggered by S3."
  runtime = var.lambda_runtime
  handler = var.lambda_handler_function

  # where to find the source code (CI/CD pipeline uploads it there)
  s3_bucket = var.lambda_sources_bucket_name
  s3_key = var.manage_book_icons_lambda_source_object_key

  environment {
    BOOK_ICONS_BUCKET_NAME = var.book_icons_bucket.name
  }
}

# allow book images S3 bucket to trigger this lambda
resource "aws_lambda_permission" "s3_lambda_trigger_permission" {
  statement_id  = "AllowS3ToInvokeLambda"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.manage_book_icons_lambda.function_name
  principal     = "s3.amazonaws.com"
  source_arn    = var.book_images_bucket.arn
}

# set object create and delete events to trigger this lambda
resource "aws_s3_bucket_notification" "s3_lambda_trigger" {
  bucket = var.book_images_bucket.name
  lambda_function {
    lambda_function_arn = aws_lambda_function.manage_book_icons_lambda.arn
    events = [
      "s3:ObjectCreated:*",
      "s3:ObjectRemoved:*"
    ]
  }
}

