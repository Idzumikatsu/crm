#!/bin/sh
# Generate nginx configuration from template with environment variables.
set -e

# Provide sane defaults for local runs or smoke tests.
: "${APP_HOST:=app}"
: "${APP_PORT:=8080}"
: "${SERVER_NAME:=localhost}"

# Render the final configuration file.
envsubst '${SERVER_NAME} ${APP_HOST} ${APP_PORT} ${NGINX_HTTP_PORT} ${NGINX_HTTPS_PORT}' \
  < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

# Run the provided command (defaults to "nginx -g 'daemon off;'").
exec "$@"
