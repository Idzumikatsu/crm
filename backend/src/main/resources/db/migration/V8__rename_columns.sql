ALTER TABLE lesson
    RENAME COLUMN start_ts TO date_time;
ALTER TABLE lesson
    RENAME COLUMN duration_min TO duration;
-- Drop end_ts if not needed
ALTER TABLE lesson
    DROP COLUMN IF EXISTS end_ts;
