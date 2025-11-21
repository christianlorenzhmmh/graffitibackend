-- Create graffiti table
CREATE TABLE IF NOT EXISTS graffiti (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(500),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    photo_path VARCHAR(500),
    status VARCHAR(50) DEFAULT 'reported',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create index on status for faster filtering
CREATE INDEX IF NOT EXISTS idx_graffiti_status ON graffiti(status);

-- Create index on created_at for sorting
CREATE INDEX IF NOT EXISTS idx_graffiti_created_at ON graffiti(created_at DESC);
