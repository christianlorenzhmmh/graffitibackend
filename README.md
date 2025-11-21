# Graffiti Backend API

A RESTful API backend for managing graffiti reports with photo uploads, built with Java, Spring Boot, and PostgreSQL.

## Features

- 🗄️ PostgreSQL database for data storage with JPA/Hibernate
- 🔄 Flyway for database migrations
- 📸 Photo upload to filesystem with Spring MultipartFile
- 🔒 File validation (images only, 10MB limit)
- 🚀 RESTful API endpoints
- ✅ CORS enabled
- 🔍 Query filtering and pagination
- 🛡️ Exception handling and validation
- 🧪 Unit tests with JUnit and MockMvc

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Installation

1. Clone the repository:
```bash
git clone https://github.com/christianlorenzhmmh/graffitibackend.git
cd graffitibackend
```

2. Create a `.env` file or set environment variables:
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=graffitidb
export DB_USER=postgres
export DB_PASSWORD=your_password
```

3. Create the PostgreSQL database:
```bash
createdb graffitidb
```

Note: Database tables will be created automatically by Flyway migrations when you start the application for the first time.

4. Build the project:
```bash
mvn clean install
```

## Usage

### Running the Application

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:3000`.

### Running Tests

```bash
mvn test
```

## API Endpoints

### Health Check
- **GET** `/api/health` - Returns server status

### Graffiti Management
- **POST** `/api/graffiti` - Create new graffiti entry with photo
- **GET** `/api/graffiti` - List all graffiti entries (supports pagination and filtering)
- **GET** `/api/graffiti/{id}` - Get single graffiti entry
- **PUT** `/api/graffiti/{id}` - Update graffiti entry
- **DELETE** `/api/graffiti/{id}` - Delete graffiti entry
- **GET** `/uploads/{filename}` - Serve uploaded photos

See full API documentation in the detailed README sections above.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Flyway** - Database migrations
- **PostgreSQL**
- **Lombok**
- **JUnit 5**
- **Maven**

## Database Migrations

This application uses Flyway for database schema management. Migration files are located in `src/main/resources/db/migration/`.

### Migration Naming Convention
- `V1__Create_graffiti_table.sql` - Initial schema
- `V2__Add_new_column.sql` - Example for future migrations

### Creating New Migrations
1. Create a new SQL file in `src/main/resources/db/migration/`
2. Name it following the pattern: `V{version}__{description}.sql`
3. Add your SQL DDL statements
4. Restart the application - Flyway will automatically apply the migration

### Migration Commands
```bash
# Migrate database (happens automatically on app start)
mvn spring-boot:run

# Get migration info
mvn flyway:info

# Validate migrations
mvn flyway:validate
```

## License

MIT
