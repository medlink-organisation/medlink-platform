ALTER TABLE comptes_rendus ALTER COLUMN praticien_id DROP NOT NULL;
ALTER TABLE comptes_rendus ALTER COLUMN contenu DROP NOT NULL;
ALTER TABLE comptes_rendus ADD COLUMN praticien_business_id BIGINT;
ALTER TABLE comptes_rendus ADD COLUMN clinique_id BIGINT;
ALTER TABLE comptes_rendus ADD COLUMN date_consultation TIMESTAMP;
ALTER TABLE comptes_rendus ADD COLUMN motif TEXT;
ALTER TABLE comptes_rendus ADD COLUMN symptomes TEXT;
ALTER TABLE comptes_rendus ADD COLUMN diagnostic TEXT;
ALTER TABLE comptes_rendus ADD COLUMN observations TEXT;
ALTER TABLE comptes_rendus ADD COLUMN traitement_recommande TEXT;
ALTER TABLE comptes_rendus ADD COLUMN examens_complementaires TEXT;
ALTER TABLE comptes_rendus ADD COLUMN recommandations TEXT;
ALTER TABLE comptes_rendus ADD COLUMN controle_requis BOOLEAN;

CREATE INDEX idx_comptes_rendus_praticien_business_id
    ON comptes_rendus (praticien_business_id);

ALTER TABLE comptes_rendus_aud ADD COLUMN praticien_business_id BIGINT;
ALTER TABLE comptes_rendus_aud ADD COLUMN clinique_id BIGINT;
ALTER TABLE comptes_rendus_aud ADD COLUMN date_consultation TIMESTAMP;
ALTER TABLE comptes_rendus_aud ADD COLUMN motif TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN symptomes TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN diagnostic TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN observations TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN traitement_recommande TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN examens_complementaires TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN recommandations TEXT;
ALTER TABLE comptes_rendus_aud ADD COLUMN controle_requis BOOLEAN;
