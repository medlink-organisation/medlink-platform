CREATE UNIQUE INDEX IF NOT EXISTS uk_rendez_vous_active_creneau
    ON rendez_vous(creneau_id)
    WHERE status IN ('EN_ATTENTE', 'CONFIRME');
