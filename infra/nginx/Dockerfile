FROM nginx:1.27-alpine
COPY nginx.conf.template /etc/nginx/nginx.conf.template
CMD sh -c "envsubst '${APP_HOST} ${APP_PORT} ${SERVER_NAME}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf && nginx -g 'daemon off;'"
