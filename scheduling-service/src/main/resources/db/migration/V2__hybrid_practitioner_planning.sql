CREATE TABLE IF NOT EXISTS plannings_hebdomadaires (
    id BIGSERIAL PRIMARY KEY,
    praticien_id BIGINT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE,
    duree_consultation INTEGER NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS plages_travail (
    id BIGSERIAL PRIMARY KEY,
    planning_id BIGINT NOT NULL REFERENCES plannings_hebdomadaires(id) ON DELETE CASCADE,
    jour VARCHAR(16) NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    clinique_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS exceptions_planning (
    id BIGSERIAL PRIMARY KEY,
    praticien_id BIGINT NOT NULL,
    date_exception DATE NOT NULL,
    type VARCHAR(32) NOT NULL,
    motif VARCHAR(500),
    version BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT uk_exception_praticien_date UNIQUE (praticien_id, date_exception)
);

CREATE TABLE IF NOT EXISTS plages_exceptionnelles (
    id BIGSERIAL PRIMARY KEY,
    exception_id BIGINT NOT NULL REFERENCES exceptions_planning(id) ON DELETE CASCADE,
    clinique_id BIGINT NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

ALTER TABLE creneaux ADD COLUMN IF NOT EXISTS planning_id BIGINT;
ALTER TABLE creneaux ADD COLUMN IF NOT EXISTS exception_id BIGINT;
ALTER TABLE creneaux_aud ADD COLUMN IF NOT EXISTS planning_id BIGINT;
ALTER TABLE creneaux_aud ADD COLUMN IF NOT EXISTS exception_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_planning_praticien_dates ON plannings_hebdomadaires(praticien_id, date_debut, date_fin);
CREATE INDEX IF NOT EXISTS idx_plage_planning_jour ON plages_travail(planning_id, jour);
CREATE UNIQUE INDEX IF NOT EXISTS uk_creneau_praticien_dates ON creneaux(praticien_id, date_debut, date_fin);

CREATE TABLE IF NOT EXISTS plannings_hebdomadaires_aud (
    rev INTEGER NOT NULL REFERENCES revinfo(rev), revtype SMALLINT, id BIGINT NOT NULL,
    praticien_id BIGINT, date_debut DATE, date_fin DATE, duree_consultation INTEGER, actif BOOLEAN,
    PRIMARY KEY (rev, id)
);
CREATE TABLE IF NOT EXISTS plages_travail_aud (
    rev INTEGER NOT NULL REFERENCES revinfo(rev), revtype SMALLINT, id BIGINT NOT NULL,
    planning_id BIGINT, jour VARCHAR(16), heure_debut TIME, heure_fin TIME, clinique_id BIGINT,
    PRIMARY KEY (rev, id)
);
CREATE TABLE IF NOT EXISTS exceptions_planning_aud (
    rev INTEGER NOT NULL REFERENCES revinfo(rev), revtype SMALLINT, id BIGINT NOT NULL,
    praticien_id BIGINT, date_exception DATE, type VARCHAR(32), motif VARCHAR(500),
    PRIMARY KEY (rev, id)
);
CREATE TABLE IF NOT EXISTS plages_exceptionnelles_aud (
    rev INTEGER NOT NULL REFERENCES revinfo(rev), revtype SMALLINT, id BIGINT NOT NULL,
    exception_id BIGINT, clinique_id BIGINT, heure_debut TIME, heure_fin TIME,
    PRIMARY KEY (rev, id)
);
