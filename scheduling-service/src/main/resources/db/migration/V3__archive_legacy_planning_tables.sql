-- Les créneaux déjà générés et les rendez-vous restent inchangés.
-- Les anciennes configurations sont archivées au lieu d'être supprimées.
ALTER TABLE IF EXISTS plages_travail RENAME TO legacy_plages_travail;
ALTER TABLE IF EXISTS plannings_hebdomadaires RENAME TO legacy_plannings_hebdomadaires;
ALTER TABLE IF EXISTS plages_exceptionnelles RENAME TO legacy_plages_exceptionnelles;
ALTER TABLE IF EXISTS exceptions_planning RENAME TO legacy_exceptions_planning;
ALTER TABLE IF EXISTS plages_travail_aud RENAME TO legacy_plages_travail_aud;
ALTER TABLE IF EXISTS plannings_hebdomadaires_aud RENAME TO legacy_plannings_hebdomadaires_aud;
ALTER TABLE IF EXISTS plages_exceptionnelles_aud RENAME TO legacy_plages_exceptionnelles_aud;
ALTER TABLE IF EXISTS exceptions_planning_aud RENAME TO legacy_exceptions_planning_aud;
