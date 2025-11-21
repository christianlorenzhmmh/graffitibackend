# Graffiti Backend API

A RESTful API backend for managing graffiti reports with photo uploads, built with Node.js, Express, and PostgreSQL.

## Features

- 🗄️ PostgreSQL database for data storage
- 📸 Photo upload to filesystem with multer
- 🔒 File validation (images only, 10MB limit)
- 🚀 RESTful API endpoints
- ✅ CORS enabled
- 🔍 Query filtering and pagination

## Prerequisites

- Node.js (v14 or higher)
- PostgreSQL (v12 or higher)
- npm or yarn

## Installation

1. Clone the repository:
```bash
git clone https://github.com/christianlorenzhmmh/graffitibackend.git
cd graffitibackend
```

2. Install dependencies:
```bash
npm install
```

3. Create a `.env` file based on `.env.example`:
```bash
cp .env.example .env
```

4. Update the `.env` file with your PostgreSQL credentials:
```env
PORT=3000
DB_HOST=localhost
DB_PORT=5432
DB_NAME=graffitidb
DB_USER=postgres
DB_PASSWORD=your_password
UPLOAD_DIR=uploads
```

5. Create the PostgreSQL database:
```bash
createdb graffitidb
```

6. Initialize the database schema:
```bash
npm run init-db
```

## Usage

### Development Mode
```bash
npm run dev
```

### Production Mode
```bash
npm start
```

The server will start on `http://localhost:3000` (or the port specified in your `.env` file).

## API Endpoints

### Health Check
```
GET /api/health
```
Returns server status.

### Create Graffiti Entry
```
POST /api/graffiti
Content-Type: multipart/form-data

Fields:
- title (required): Title of the graffiti report
- description (optional): Detailed description
- location (optional): Location description
- latitude (optional): Latitude coordinate
- longitude (optional): Longitude coordinate
- status (optional): Status (default: "reported")
- photo (optional): Image file (JPEG, PNG, GIF, WebP, max 10MB)
```

Example using curl:
```bash
curl -X POST http://localhost:3000/api/graffiti \
  -F "title=Street Art on Main St" \
  -F "description=Large mural on building wall" \
  -F "location=123 Main Street" \
  -F "latitude=40.7128" \
  -F "longitude=-74.0060" \
  -F "status=reported" \
  -F "photo=@/path/to/image.jpg"
```

### List Graffiti Entries
```
GET /api/graffiti?status=reported&limit=100&offset=0

Query Parameters:
- status (optional): Filter by status
- limit (optional): Number of results (default: 100)
- offset (optional): Pagination offset (default: 0)
```

### Get Single Graffiti Entry
```
GET /api/graffiti/:id
```

### Update Graffiti Entry
```
PUT /api/graffiti/:id
Content-Type: multipart/form-data

Fields: Same as POST (all optional)
```

### Delete Graffiti Entry
```
DELETE /api/graffiti/:id
```
Deletes the entry and associated photo file.

### Get Uploaded Photo
```
GET /uploads/:filename
```
Serves the uploaded photo files.

## Database Schema

```sql
CREATE TABLE graffiti (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(500),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    photo_path VARCHAR(500),
    status VARCHAR(50) DEFAULT 'reported',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Response Format

All API responses follow this structure:

Success:
```json
{
  "success": true,
  "data": { ... }
}
```

Error:
```json
{
  "error": "Error message"
}
```

## Error Handling

- 400: Bad Request (validation errors)
- 404: Not Found
- 500: Internal Server Error

## File Upload Restrictions

- Allowed formats: JPEG, JPG, PNG, GIF, WebP
- Maximum file size: 10MB
- Files are stored in the `uploads/` directory
- Files are automatically deleted when the associated entry is deleted

## Testing

Run tests with:
```bash
npm test
```

## Project Structure

```
graffitibackend/
├── server.js          # Main application file
├── db.js              # Database connection
├── init-db.js         # Database initialization script
├── schema.sql         # Database schema
├── package.json       # Dependencies and scripts
├── .env.example       # Environment variables template
├── .gitignore         # Git ignore rules
├── README.md          # This file
└── uploads/           # Uploaded photos (created automatically)
```

## License

MIT