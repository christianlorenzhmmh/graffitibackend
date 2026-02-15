-- V4__user_table.sql
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255), -- für klassische Logins, kann bei Passkey NULL sein
    role VARCHAR(32) NOT NULL,  -- ADMIN, POWERUSER, STANDARD
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    webauthn_id VARCHAR(255) -- für späteres Passkey/WebAuthn-Upgrade, jetzt NULL
);

CREATE INDEX idx_app_user_email ON app_user(email);