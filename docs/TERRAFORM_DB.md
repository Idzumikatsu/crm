# Provisioning PostgreSQL with Terraform

This guide demonstrates how to create a production-grade PostgreSQL instance
using reusable Terraform modules. The configuration assumes a Kubernetes
cluster, but the same variables work for cloud providers as well.

## Required variables

| Name | Description |
| --- | --- |
| `db_name` | Name of the database to create |
| `db_user` | Username that will own the database |
| `storage_gb` | Size of the persistent volume in gigabytes |

## Module example

```hcl
module "postgresql" {
  source       = "terraform-module/postgresql/kubernetes"
  version      = "1.0.0"

  name         = var.db_name
  username     = var.db_user
  storage_size = var.storage_gb
}

output "db_host" {
  value = module.postgresql.host
}

output "db_password" {
  value     = module.postgresql.password
  sensitive = true
}
```

Run `terraform init` followed by `terraform apply` to provision the
resources. After completion Terraform prints the connection details:

```text
db_host     = "postgresql.internal"
db_password = "s3cr3t-password"
```

Save these values in `infra/.env` so the application can connect to the new
instance.
