# Terraform — AWS infrastructure

Infrastructure as Code for deploying `slzvieira-news` to AWS Fargate, fronted by an Application Load Balancer and reachable at a custom domain via Route 53.

This is **not** wired into any CI/CD pipeline — it's meant to be run manually from your machine.

## Architecture

```
Route 53 (Alias A record)
        │
        ▼
   ALB (443, HTTPS, ACM certificate)
        │  listener redirects 80 → 443
        ▼
   Target Group (target_type = ip)
        │
        ▼
ECS Service (Fargate) ── private subnets
        │
        ▼
   Task (container, port 8080) ── image pulled from ECR via NAT Gateway
```

- The ALB sits in **public subnets** and is the only internet-facing component.
- Fargate tasks run in **private subnets**; they're only reachable from the ALB's security group.
- A NAT Gateway gives the private subnets outbound internet access (needed to pull images from ECR and reach AWS APIs).
- TLS terminates at the ALB — the container itself only speaks plain HTTP on port 8080.

## Files

| File | Purpose |
|---|---|
| `versions.tf` | Terraform version constraint, AWS provider version, commented-out S3 backend |
| `variables.tf` | All configurable inputs (region, domain, CIDRs, task size, image tag, etc.) |
| `network.tf` | VPC, public/private subnets, Internet Gateway, NAT Gateway, route tables, security groups |
| `ecr.tf` | ECR repository the application image is pushed to |
| `acm.tf` | ACM certificate for `domain_name`, validated automatically via a Route 53 DNS record |
| `alb.tf` | Application Load Balancer, target group (health check on `/news/v1/random`), HTTPS listener (443) and HTTP→HTTPS redirect (80→443) |
| `ecs.tf` | ECS cluster, task definition, IAM execution role, CloudWatch log group, ECS service (Fargate) |
| `route53.tf` | Alias A record pointing `domain_name` at the ALB |
| `outputs.tf` | ECR repository URL, ALB DNS name, service URL, cluster/service names |
| `terraform.tfvars.example` | Template for the variables you must fill in yourself |

## Prerequisites

- An existing Route 53 hosted zone for `slzvieira.com`, with its zone ID at hand.
- AWS credentials configured locally (`aws configure` or equivalent) with permissions to manage VPC, ECS, ECR, ALB, ACM, Route 53 and IAM resources.
- The application image already built (see the root [README.md](../README.md#docker-image-jib)) and pushed to the ECR repository this stack creates (`terraform output ecr_repository_url` after the first apply).

## Usage

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
# edit terraform.tfvars — route53_zone_id has no default and must be set

terraform init
terraform plan
terraform apply
```

`terraform.tfvars` is gitignored — it's meant to hold account-specific values and should never be committed.

### Deploying a new image

1. Build and push the image to the ECR repository (e.g. `./mvnw clean package jib:build -Dimage=<ecr_repository_url>:<tag>`).
2. If `image_tag` changed, update it in `terraform.tfvars` and run `terraform apply` again to roll the ECS service to the new task definition.

### Tearing down

```bash
terraform destroy
```

This removes every resource created by this stack, including the NAT Gateway (which incurs hourly cost even when idle — worth destroying between uses for a personal project).

## Notes and trade-offs

- **No remote backend configured by default.** State is local (`terraform.tfstate` in this directory, gitignored). Fine for solo use; uncomment the `backend "s3"` block in `versions.tf` if you want shared/remote state.
- **Single NAT Gateway.** Cheaper, but is a single point of failure across AZs — acceptable for a personal project, not for something requiring high availability.
- **`desired_count = 1` by default.** No auto-scaling configured; the service always runs a single task unless you change `desired_count` or add an Application Auto Scaling policy.
- **Health check path is `/news/v1/random`.** If that endpoint's behavior changes (e.g. starts returning non-200 under normal conditions), update the `health_check` block in `alb.tf` accordingly.
