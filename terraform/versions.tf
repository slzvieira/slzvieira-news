terraform {
  required_version = ">= 1.7"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Uncomment and configure a remote backend before running this in a shared/production context.
  # backend "s3" {
  #   bucket = "your-tf-state-bucket"
  #   key    = "slzvieira-news/terraform.tfstate"
  #   region = "us-east-1"
  # }
}
