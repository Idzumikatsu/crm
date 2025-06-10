#!/usr/bin/env sh
set -e
: "${APP_HOST:?APP_HOST is required}"
: "${APP_PORT:?APP_PORT is required}"
: "${SERVER_NAME:?SERVER_NAME is required}"
envsubst '${APP_HOST} ${APP_PORT} ${SERVER_NAME}' \
  < infra/nginx/nginx.conf.template \
  > infra/nginx/nginx.conf
