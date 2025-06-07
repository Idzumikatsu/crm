ALTER TABLE lesson
    DROP COLUMN IF EXISTS student_id,
    ADD COLUMN group_id UUID NOT NULL REFERENCES groups(id);
