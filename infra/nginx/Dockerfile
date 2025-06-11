FROM nginx:1.27-alpine
RUN apk add --no-cache gettext

# Copy the template for the final configuration
COPY nginx.conf.template /etc/nginx/nginx.conf.template

# Create the entrypoint script directly during the image build so
# deployments don't rely on an external file being present.
RUN cat <<'EOF' >/usr/local/bin/docker-entrypoint.sh \
    && chmod +x /usr/local/bin/docker-entrypoint.sh
#!/bin/sh
set -e

# Provide defaults for local runs or smoke tests
: "${APP_HOST:=app}"
: "${APP_PORT:=8080}"
: "${SERVER_NAME:=localhost}"

# Render the configuration and execute the requested command
envsubst '$APP_HOST $APP_PORT $SERVER_NAME' \
  < /etc/nginx/nginx.conf.template \
  > /etc/nginx/nginx.conf
exec "$@"
EOF

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
