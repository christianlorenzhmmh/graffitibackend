# Graffiti Backend API

A RESTful API backend for managing graffiti reports with photo uploads, built with Java, Spring Boot, and PostgreSQL.

## Features

- 🗄️ PostgreSQL database for data storage with JPA/Hibernate
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
- **PostgreSQL**
- **Lombok**
- **JUnit 5**
- **Maven**

## License

MIT
