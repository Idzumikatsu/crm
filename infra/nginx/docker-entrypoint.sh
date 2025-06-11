#!/bin/sh
# Generate nginx configuration from template with environment variables
set -e
envsubst '$APP_HOST $APP_PORT $SERVER_NAME' \
  < /etc/nginx/nginx.conf.template \
  > /etc/nginx/nginx.conf
exec nginx -g 'daemon off;'
