#!/bin/sh
# Generate Maven proxy settings from environment
set -e

# Use default proxy values when none are provided
PROXY_HOST="${PROXY_HOST:-proxy}"
PROXY_PORT="${PROXY_PORT:-8080}"
export PROXY_HOST PROXY_PORT

mkdir -p .mvn
envsubst < .mvn/settings.xml.in > .mvn/settings.xml
echo "Generated .mvn/settings.xml for $PROXY_HOST:$PROXY_PORT"
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
