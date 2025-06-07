CREATE TABLE teacher_settings (
    teacher_id UUID PRIMARY KEY REFERENCES teachers(id),
    buffer_min INTEGER,
    template TEXT
);
