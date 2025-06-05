-- src/main/resources/db/migration/V1__init_schema.sql

-- Пример создания таблицы пользователей (пример, можно заменить на реальную доменную модель)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- Пример создания таблицы расписания (Schedule)
CREATE TABLE IF NOT EXISTS schedules (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);
