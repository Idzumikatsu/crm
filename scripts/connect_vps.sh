#!/usr/bin/env bash
#
# Подключает контейнер Codex / GitHub Actions к VPS по SSH-ключу,
# взятому из secrets репозитория.  Проверяет доступ одной диагностической
# командой и удаляет ключ после завершения.

set -euo pipefail

### 1. Проверяем, что все переменные окружения заданы
: "${VPS_HOST:?Не задан VPS_HOST}"
: "${VPS_USER:?Не задан VPS_USER}"
: "${VPS_SSH_KEY:?Не задан VPS_SSH_KEY}"
: "${VPS_SSH_PORT:=22}"             # порт по-умолчанию
: "${VPS_PASSPHRASE:=}"             # может быть пустым

### 2. Создаём временный файл с приватным ключом
key_file="$(mktemp)"
chmod 600 "${key_file}"
printf '%s\n' "${VPS_SSH_KEY}" > "${key_file}"

### 3. (Необязательно) доверяем fingerprint сервера,
###    чтобы ssh не задавал вопросов в non-interactive среде
mkdir -p ~/.ssh
chmod 700 ~/.ssh
ssh-keyscan -p "${VPS_SSH_PORT}" "${VPS_HOST}" >> ~/.ssh/known_hosts 2>/dev/null

### 4. Выполняем простую тест-команду на VPS
SSH_ARGS=(
  -i "${key_file}"
  -p "${VPS_SSH_PORT}"
  -o ServerAliveInterval=15          # держим соединение открытым
  -o StrictHostKeyChecking=yes       # known_hosts уже заполнен
)
ssh "${SSH_ARGS[@]}" \
    "${VPS_USER}@${VPS_HOST}" \
    'echo "✅  SSH connection established on $(hostname)"; uname -a'

### 5. Удаляем приватный ключ и выходим
shred --remove --zero -- "${key_file}"
echo "Finished; temporary key removed."
