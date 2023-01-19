# Provision the book images and icons S3 buckets

resource "aws_s3_bucket" "images_bucket" {
  bucket = "${var.book_images_bucket_name}-${var.aws_region}-${var.environment}"
  force_destroy = true # to be able to delete the bucket even with objects in it
}

resource "aws_s3_bucket" "icons_bucket" {
  bucket = "${var.book_icons_bucket_name}-${var.aws_region}-${var.environment}"
  force_destroy = true
}

locals {
  bucket_ids = [aws_s3_bucket.images_bucket.id, aws_s3_bucket.icons_bucket.id]
  bucket_arns = [aws_s3_bucket.images_bucket.arn, aws_s3_bucket.icons_bucket.arn]
}

resource "aws_s3_bucket_versioning" "bucket_versionings" {
  count = length(local.bucket_ids)
  bucket = element(local.bucket_ids, count.index)
  versioning_configuration {
    status = "Disabled"
  }
}

data "aws_iam_policy_document" "bucket_policies" {
  count = length(local.bucket_arns)
  statement { # only terraform user should be able to modify this bucket
    sid = "BucketManagement"
    effect = "Deny"
    principals {
      identifiers = ["*"]
      type        = "*"
    }
    actions = [ "s3:DeleteBucket" ]
    resources = [
      element(local.bucket_arns, count.index)
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
      "${element(local.bucket_arns, count.index)}/*"
    ]
  }
  # Iam role is created in 'ec2' module. This role will allow Ec2 instance to access this bucket
}

resource "aws_s3_bucket_policy" "bucket_policy_attachments" {
  count = length(local.bucket_ids)
  bucket = element(local.bucket_ids, count.index)
  policy = data.aws_iam_policy_document.bucket_policies[count.index].json
}

