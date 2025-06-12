FROM nginx:1.27-alpine
# install tools needed to render config and generate certificates
RUN apk add --no-cache gettext openssl

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

# If certificate files are missing, generate a self-signed pair so that
# nginx can start even in development environments where no real certs
# are provided.
if [ ! -f /etc/nginx/certs/fullchain.pem ] || [ ! -f /etc/nginx/certs/crm-synergy.key ]; then
    echo "Generating self-signed certificate for $SERVER_NAME" >&2
    mkdir -p /etc/nginx/certs
    openssl req -x509 -newkey rsa:2048 -nodes \
        -keyout /etc/nginx/certs/crm-synergy.key \
        -out /etc/nginx/certs/crm-synergy.crt \
        -days 365 -subj "/CN=$SERVER_NAME"
    cp /etc/nginx/certs/crm-synergy.crt /etc/nginx/certs/crm-synergy_ca.crt
    cat /etc/nginx/certs/crm-synergy.crt /etc/nginx/certs/crm-synergy_ca.crt > /etc/nginx/certs/fullchain.pem
fi

# Render the configuration and execute the requested command
envsubst '$APP_HOST $APP_PORT $SERVER_NAME' \
  < /etc/nginx/nginx.conf.template \
  > /etc/nginx/nginx.conf
exec "$@"
EOF

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
