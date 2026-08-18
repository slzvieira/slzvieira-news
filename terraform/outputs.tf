output "ecr_repository_url" {
  description = "Push images here before deploying (e.g. via Jib's jib:build)"
  value       = aws_ecr_repository.app.repository_url
}

output "alb_dns_name" {
  description = "ALB DNS name (useful for debugging before DNS propagates)"
  value       = aws_lb.app.dns_name
}

output "service_url" {
  description = "Public URL of the service"
  value       = "https://${var.domain_name}"
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  value = aws_ecs_service.app.name
}
