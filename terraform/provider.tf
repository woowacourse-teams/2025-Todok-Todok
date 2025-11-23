terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.12"
    }
  }

  required_version = ">= 1.2"
}

provider "aws" {
  region = "ap-northeast-2"
  profile = "todoktodok"
}
