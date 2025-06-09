CREATE TABLE verification_token (
    token UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id)
);
