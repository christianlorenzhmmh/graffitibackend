# graffitibackend

A Spring Boot REST API for managing graffiti photos and their metadata.

## Features

- Upload photos taken on mobile devices
- Store metadata for graffiti locations (title, tags, coordinates)
- Retrieve all graffiti data
- Input validation for file uploads and metadata
- H2 in-memory database with JPA relationships

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database
- Maven

## API Endpoints

### 1. Upload Graffiti Photo
Upload a photo file and receive a unique photo ID.

**Endpoint:** `POST /api/uploadGraffitiPhoto`

**Request:**
- Content-Type: `multipart/form-data`
- Parameter: `file` (image file)

**Response:**
```json
{
  "photoId": 1
}
```

**Validation:**
- File must not be null or empty
- File must be an image (content type must start with "image/")

### 2. Add Graffiti Data
Add metadata for an uploaded graffiti photo.

**Endpoint:** `POST /api/add-graffiti-data`

**Request:**
- Content-Type: `application/json`

```json
{
  "title": "Amazing Street Art",
  "photoId": 1,
  "tags": "urban,colorful,street",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "altitude": 10.5
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Amazing Street Art",
  "photo": {...},
  "tags": "urban,colorful,street",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "altitude": 10.5
}
```

**Validation:**
- `title` must not be null or empty
- `photoId` must not be null and must reference an existing photo
- `latitude` must be between -90 and 90
- `longitude` must be between -180 and 180

### 3. Load All Data
Retrieve all graffiti photos and metadata.

**Endpoint:** `GET /api/loadAllData`

**Response:**
```json
{
  "graffitiPhotos": [
    {
      "id": 1,
      "fileName": "photo.jpg",
      "contentType": "image/jpeg"
    }
  ],
  "graffitiMetadata": [
    {
      "id": 1,
      "title": "Amazing Street Art",
      "photoId": 1,
      "tags": "urban,colorful,street",
      "latitude": 40.7128,
      "longitude": -74.0060,
      "altitude": 10.5
    }
  ]
}
```

**Note:** Photo data (bytes) is not included in this response for performance optimization.

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

### Run Tests
```bash
mvn test
```

## Configuration

The application uses an H2 in-memory database by default. Configuration can be modified in `src/main/resources/application.properties`:

- Database URL: `jdbc:h2:mem:graffitidb`
- Server Port: `8080`
- Max File Upload Size: `10MB`

## Development

### H2 Console
The H2 database console is available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:graffitidb`
- Username: `sa`
- Password: (empty)

## Testing

The project includes comprehensive integration tests covering:
- Photo upload functionality
- Metadata storage
- Data retrieval
- Input validation
- Invalid file type handling
- Invalid coordinate handling

All tests use an in-memory database and MockMvc for API testing.
