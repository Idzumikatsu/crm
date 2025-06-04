#!/bin/sh
# Generate Maven proxy settings from environment
set -e

# Determine proxy host and port from PROXY_HOST/PROXY_PORT or HTTP(S)_PROXY
if [ -z "$PROXY_HOST" ] || [ -z "$PROXY_PORT" ]; then
  # Attempt to derive from HTTP_PROXY if available (format http://host:port)
  if [ -n "$HTTP_PROXY" ]; then
    PROXY_HOST=$(echo "$HTTP_PROXY" | sed -E 's#^https?://([^:/]+).*#\1#')
    PROXY_PORT=$(echo "$HTTP_PROXY" | sed -E 's#^https?://[^:/]+:([0-9]+).*#\1#')
  fi
fi

if [ -n "$PROXY_HOST" ] && [ -n "$PROXY_PORT" ]; then
  export PROXY_HOST PROXY_PORT
  mkdir -p .mvn
  envsubst < .mvn/settings.xml.in > .mvn/settings.xml
  echo "Generated .mvn/settings.xml for $PROXY_HOST:$PROXY_PORT"
else
  mkdir -p .mvn
  cat > .mvn/settings.xml <<EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
</settings>
EOF
  echo "Proxy variables not set; generated empty .mvn/settings.xml"
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
