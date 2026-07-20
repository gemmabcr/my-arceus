ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL,
    ALTER COLUMN password TYPE VARCHAR(255);

CREATE UNIQUE INDEX users_email_unique_lower_idx ON users (LOWER(email));

CREATE TABLE user_identities
(
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    provider         VARCHAR(20)  NOT NULL,
    provider_subject VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT user_identities_provider_subject_unique UNIQUE (provider, provider_subject)
);
