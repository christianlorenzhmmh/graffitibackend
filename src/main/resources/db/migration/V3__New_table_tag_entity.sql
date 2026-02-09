-- Migration V3: TagEntity und 1:n-Relation zu GraffitiEntity

-- Neue Tabelle für Tags
CREATE TABLE IF NOT EXISTS tag (
    id BIGSERIAL PRIMARY KEY,
    value VARCHAR(255) NOT NULL UNIQUE
);

-- Spalte 'tag_id' in Graffiti-Tabelle hinzufügen (1:n-Relation)
ALTER TABLE graffiti ADD COLUMN IF NOT EXISTS tag_id BIGINT;
ALTER TABLE graffiti ADD CONSTRAINT fk_graffiti_tag FOREIGN KEY (tag_id) REFERENCES tag(id);

-- Altes Feld 'tag' entfernen, falls vorhanden
ALTER TABLE graffiti DROP COLUMN IF EXISTS tag;

