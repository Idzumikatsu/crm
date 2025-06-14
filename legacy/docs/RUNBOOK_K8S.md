# Kubernetes Incident Runbook

This document outlines common troubleshooting steps for running the application on a Kubernetes cluster.

## Check Pod status
1. List pods in the namespace:
   ```bash
   kubectl get pods -n schedule
   ```
2. Inspect a failing pod:
   ```bash
   kubectl describe pod <name> -n schedule
   kubectl logs <name> -n schedule
   ```

## Verify database and RabbitMQ
1. Ensure StatefulSets are healthy:
   ```bash
   kubectl get statefulsets -n schedule
   ```
2. Check persistent volumes:
   ```bash
   kubectl get pvc -n schedule
   ```

## Rolling back a release
1. List Helm revisions:
   ```bash
   helm history schedule-app
   ```
2. Roll back to a previous revision:
   ```bash
   helm rollback schedule-app <revision>
   ```

## Cleaning up failed deployments
1. Delete an unsuccessful release:
   ```bash
   helm uninstall schedule-app
   ```
2. Remove remaining Jobs and pods:
   ```bash
   kubectl delete jobs --selector=app.kubernetes.io/name=schedule-app -n schedule
   ```
