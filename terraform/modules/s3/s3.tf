# Provision the book images S3 bucket

resource "aws_s3_bucket" "bucket" {
  name = "${var.bucket_name}-${var.aws_region}-${var.environment}"
  force_destroy = true # to be able to delete the bucket even with objects in it
}

resource "aws_s3_bucket_versioning" "bucket_versioning" {
  bucket = aws_s3_bucket.bucket.id
  versioning_configuration {
    status = "Disabled"
  }
}

data "aws_iam_policy_document" "bucket_policy" {
  statement { # only terraform user should be able to modify this bucket
    sid = "BucketManagement"
    effect = "Deny"
    principals {
      identifiers = ["*"]
      type        = "*"
    }
    not_actions = [
      "s3:List*"
    ]
    resources = [
      aws_s3_bucket.bucket.arn
    ]
    condition {
      test     = "StringNotEquals"
      values   = [
        "arn:aws:iam::${var.aws_account_id}:user/terraform"
      ]
      variable = "aws:PrincipalArn"
    }
  }
  statement { # allow public access to GET the objects
    sid = "ObjectManagement"
    effect = "Allow"
    principals {
      identifiers = ["*"]
      type        = "*"
    }
    actions = [
      "s3:GetObject"
    ]
    resources = [
      "${aws_s3_bucket.bucket.arn}/*"
    ]
  }
  # Iam role is created in 'ec2' module. This role will allow Ec2 instance to access this bucket
}

resource "aws_s3_bucket_policy" "bucket_policy_attachment" {
  bucket = aws_s3_bucket.bucket.id
  policy = data.aws_iam_policy_document.bucket_policy.json
}

