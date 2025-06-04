#!/bin/sh
set -e
# Generate Maven proxy settings from environment
# Create settings.xml only when both host and port are provided
if [ -n "$PROXY_HOST" ] && [ -n "$PROXY_PORT" ]; then
  mkdir -p .mvn
  envsubst < .mvn/settings.xml.in > .mvn/settings.xml
  echo "Generated .mvn/settings.xml for $PROXY_HOST:$PROXY_PORT"
else
  rm -f .mvn/settings.xml
  echo "Proxy variables not set, skipping .mvn/settings.xml generation"
fi
# Configure git proxy if variables are present
if [ -n "$HTTP_PROXY" ]; then
  git config --global http.proxy "$HTTP_PROXY"
else
  git config --global --unset http.proxy 2>/dev/null || true
fi
if [ -n "$HTTPS_PROXY" ]; then
  git config --global https.proxy "$HTTPS_PROXY"
else
  git config --global --unset https.proxy 2>/dev/null || true
fi
