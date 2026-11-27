-- Apply with the service's database migration mechanism. This is additive and
-- intentionally keeps all existing Envers revision identifiers and timestamps.
CREATE TABLE IF NOT EXISTS revinfo (
    rev INTEGER PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);

ALTER TABLE revinfo ADD COLUMN IF NOT EXISTS user_id VARCHAR(255);
ALTER TABLE revinfo ADD COLUMN IF NOT EXISTS username VARCHAR(255);
ALTER TABLE revinfo ADD COLUMN IF NOT EXISTS actor_type VARCHAR(16);
ALTER TABLE revinfo ADD COLUMN IF NOT EXISTS correlation_id VARCHAR(64);

CREATE INDEX IF NOT EXISTS idx_revinfo_user_id ON revinfo (user_id);
CREATE INDEX IF NOT EXISTS idx_revinfo_revtstmp ON revinfo (revtstmp);
