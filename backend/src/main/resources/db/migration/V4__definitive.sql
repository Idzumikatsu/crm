CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    pwd TEXT NOT NULL,
    role VARCHAR(20) NOT NULL,
    tz VARCHAR(50),
    locale VARCHAR(10),
    two_fa_secret TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE teachers (
    id UUID PRIMARY KEY REFERENCES users(id),
    hourly_rate_cents INTEGER NOT NULL,
    buffer_min INTEGER NOT NULL
);

CREATE TABLE students (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(32),
    telegram VARCHAR(64),
    email VARCHAR(255),
    notes TEXT
);

CREATE TABLE availability_template (
    id UUID PRIMARY KEY,
    teacher_id UUID NOT NULL REFERENCES teachers(id),
    weekday SMALLINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    until_date DATE
);

CREATE TABLE availability_exception (
    id UUID PRIMARY KEY,
    template_id UUID REFERENCES availability_template(id),
    teacher_id UUID NOT NULL REFERENCES teachers(id),
    date DATE NOT NULL,
    reason TEXT
);

CREATE TYPE slot_status AS ENUM ('OPEN','BOOKED','BLOCKED');

CREATE TABLE time_slot (
    id UUID PRIMARY KEY,
    teacher_id UUID NOT NULL REFERENCES teachers(id),
    start_ts TIMESTAMPTZ NOT NULL,
    end_ts TIMESTAMPTZ NOT NULL,
    status slot_status NOT NULL,
    lesson_id UUID
);

CREATE TYPE lesson_status AS ENUM ('SCHEDULED','COMPLETED','CANCELLED');

CREATE TABLE lesson (
    id UUID PRIMARY KEY,
    teacher_id UUID NOT NULL REFERENCES teachers(id),
    student_id UUID NOT NULL REFERENCES students(id),
    start_ts TIMESTAMPTZ NOT NULL,
    end_ts TIMESTAMPTZ NOT NULL,
    duration_min INTEGER NOT NULL,
    status lesson_status NOT NULL,
    price_cents INTEGER NOT NULL,
    created_by UUID NOT NULL REFERENCES users(id),
    updated_by UUID NOT NULL REFERENCES users(id),
    updated_at TIMESTAMPTZ
);

CREATE TABLE notification_template (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    lang VARCHAR(5) NOT NULL,
    subject TEXT,
    body_html TEXT
);

CREATE TABLE notification_log (
    id UUID PRIMARY KEY,
    lesson_id UUID NOT NULL REFERENCES lesson(id),
    channel VARCHAR(20) NOT NULL,
    sent_at TIMESTAMPTZ,
    status VARCHAR(20),
    read_at TIMESTAMPTZ,
    payload_json JSONB
);

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    entity VARCHAR(50) NOT NULL,
    entity_id UUID,
    action VARCHAR(20) NOT NULL,
    old_json JSONB,
    new_json JSONB,
    actor_id UUID,
    ts TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX time_slot_overlap ON time_slot USING GIST (tsrange(start_ts, end_ts));
