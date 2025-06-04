#!/bin/sh
set -e
# Generate Maven proxy settings from environment
if [ -n "$PROXY_HOST" ] && [ -n "$PROXY_PORT" ]; then
  mkdir -p .mvn
  envsubst < .mvn/settings.xml.in > .mvn/settings.xml
  echo "Generated .mvn/settings.xml"
else
  echo "PROXY_HOST and PROXY_PORT must be set" >&2
  exit 1
fi
# Configure git proxy if variables are present
if [ -n "$HTTP_PROXY" ]; then
  git config --global http.proxy "$HTTP_PROXY"
fi
if [ -n "$HTTPS_PROXY" ]; then
  git config --global https.proxy "$HTTPS_PROXY"
fi
