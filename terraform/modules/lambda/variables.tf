variable "environment" {
  type = string
}

variable "application_name" {
  type = string
}

variable "lambda_runtime" {
  type = string
  default = "nodejs16.x"
}

variable "lambda_handler_function" {
  type = string
  default = "exports.handler"
}

variable "book_images_bucket" {
  type = object({
    name = string
    arn = string
  })
}

variable "book_icons_bucket" {
  type = object({
    name = string
    arn = string
  })
}

variable "lambda_sources_bucket_name" {
  type = string
}

variable "manage_book_icons_lambda_source_object_key" {
  type = string
}