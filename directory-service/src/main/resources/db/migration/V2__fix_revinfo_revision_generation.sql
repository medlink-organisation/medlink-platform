-- Older databases were initialized by Hibernate with a plain NOT NULL `rev`
-- column. Add a sequence-backed default without replacing or renumbering any
-- existing Envers revisions.
CREATE SEQUENCE IF NOT EXISTS revinfo_rev_seq;

SELECT setval(
    'revinfo_rev_seq',
    COALESCE((SELECT MAX(rev) FROM revinfo), 0) + 1,
    false
);

ALTER SEQUENCE revinfo_rev_seq OWNED BY revinfo.rev;
ALTER TABLE revinfo
    ALTER COLUMN rev SET DEFAULT nextval('revinfo_rev_seq');
