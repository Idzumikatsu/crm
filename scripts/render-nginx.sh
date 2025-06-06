#!/usr/bin/env sh
set -e
: "${APP_HOST:?APP_HOST is required}"
: "${APP_PORT:?APP_PORT is required}"

envsubst '${APP_HOST} ${APP_PORT}' < infra/nginx/nginx.conf.template > infra/nginx/nginx.conf
