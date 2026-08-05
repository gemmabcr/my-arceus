#!/usr/bin/env bash

set -euo pipefail

PROJECT_DIR="${1:-/home/ubuntu/my-arceus}"
ENV_FILE="$PROJECT_DIR/.env"

if [[ ! -f "$ENV_FILE" ]]; then
    echo "Missing environment file: $ENV_FILE" >&2
    exit 1
fi

append_secret_if_missing() {
    local key="$1"
    local bytes="$2"

    if ! grep -q "^${key}=" "$ENV_FILE"; then
        printf '%s=%s\n' "$key" "$(openssl rand -hex "$bytes")" >> "$ENV_FILE"
    fi
}

umask 077
grep -q '^GRAFANA_ADMIN_USER=' "$ENV_FILE" || printf '%s\n' 'GRAFANA_ADMIN_USER=gemma' >> "$ENV_FILE"
grep -q '^GRAFANA_DB_USER=' "$ENV_FILE" || printf '%s\n' 'GRAFANA_DB_USER=grafana_reader' >> "$ENV_FILE"
append_secret_if_missing GRAFANA_ADMIN_PASSWORD 24
append_secret_if_missing GRAFANA_SECRET_KEY 32

if ! grep -q '^GRAFANA_DB_PASSWORD=' "$ENV_FILE"; then
    read -r -s -p "Grafana PostgreSQL password: " grafana_db_password
    echo
    read -r -s -p "Repeat Grafana PostgreSQL password: " grafana_db_password_confirmation
    echo

    if [[ "$grafana_db_password" != "$grafana_db_password_confirmation" ]]; then
        echo "Passwords do not match." >&2
        exit 1
    fi
    if [[ -z "$grafana_db_password" ]]; then
        echo "The password cannot be empty." >&2
        exit 1
    fi

    printf 'GRAFANA_DB_PASSWORD=%s\n' "$grafana_db_password" >> "$ENV_FILE"
    unset grafana_db_password grafana_db_password_confirmation
fi

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

docker run --rm \
    -e PGPASSWORD="$GRAFANA_DB_PASSWORD" \
    postgres:18-alpine \
    psql \
    "host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$GRAFANA_DB_USER sslmode=require" \
    -v ON_ERROR_STOP=1 \
    -c "SELECT 1 AS grafana_connection_ok;"

echo "Grafana secrets and read-only PostgreSQL connection are ready."
