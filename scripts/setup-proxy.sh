#!/bin/sh
set -e
# Generate Maven proxy settings from environment
# Use defaults if variables are not provided
PROXY_HOST="${PROXY_HOST:-proxy}"
PROXY_PORT="${PROXY_PORT:-8080}"
export PROXY_HOST PROXY_PORT
mkdir -p .mvn
envsubst < .mvn/settings.xml.in > .mvn/settings.xml
echo "Generated .mvn/settings.xml for $PROXY_HOST:$PROXY_PORT"
# Configure git proxy if variables are present
if [ -n "$HTTP_PROXY" ]; then
  git config --global http.proxy "$HTTP_PROXY"
fi
if [ -n "$HTTPS_PROXY" ]; then
  git config --global https.proxy "$HTTPS_PROXY"
fi
