-- Clinique has no business-code column. Its name is the stable seed key.
-- This case-insensitive index prevents duplicates during concurrent startups.
CREATE UNIQUE INDEX IF NOT EXISTS uk_clinique_nom_ci
    ON clinique (LOWER(nom));
