# Finance Tracker API

A RESTful API for managing personal finances built with Java Spring Boot. Supports JWT authentication and allows authenticated users to create, read, update, and delete their own financial transactions.

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![CI Pipeline](https://github.com/gureett/finance-tracker-api/actions/workflows/ci.yml/badge.svg)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |
| Testing | JUnit 5 + Mockito + JaCoCo |
| CI/CD | GitHub Actions |

## Architecture

    src/
    ├── controller/     # HTTP request handlers
    ├── service/        # Business logic
    ├── repository/     # Database access layer
    ├── model/          # JPA entities (User, Transaction)
    ├── dto/            # Request/response objects
    ├── config/         # Security and JWT configuration
    └── exception/      # Global exception handling

## Prerequisites

- Java 25
- Maven
- Docker + Docker Compose

## Getting Started

### Option 1 — Docker Compose (Recommended)

```bash
git clone <your-repo-url>
cd finance-tracker-api
cp .env.example .env
```

Edit [.env.example](.env.example) with your values and save it as `.env`, then run:

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`. See [docker-compose.yml](docker-compose.yml) for the full configuration.

### Option 2 — Manual Setup

1. Start a PostgreSQL instance and create a database called `financetracker`

2. Configure your `application.properties` under `src/main/resources/` with your database and JWT values:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financetracker
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.application.name=finance-tracker
server.port=8080
jwt.secret=your-secret-key-at-least-32-characters-long
jwt.expiration=86400000
```

3. Run the application:

```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | Login and receive JWT token | No |

### Transactions

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/transactions` | Get all transactions | Yes |
| GET | `/api/transactions/{id}` | Get transaction by ID | Yes |
| GET | `/api/transactions/type/{type}` | Filter by INCOME or EXPENSE | Yes |
| GET | `/api/transactions/category/{category}` | Filter by category | Yes |
| POST | `/api/transactions` | Create a transaction | Yes |
| PUT | `/api/transactions/{id}` | Update a transaction | Yes |
| DELETE | `/api/transactions/{id}` | Delete a transaction | Yes |

All protected endpoints require a Bearer token:

```
Authorization: Bearer <your-jwt-token>
```

## Example Requests

**Register:**
```json
POST /api/auth/register
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "token": "eyJhbGci...",
    "email": "john@example.com",
    "name": "John Doe"
}
```

**Create a transaction:**
```json
POST /api/transactions
{
    "description": "Grocery shopping",
    "amount": 85.50,
    "category": "Food",
    "type": "EXPENSE"
}
```

Response:
```json
{
    "id": 1,
    "description": "Grocery shopping",
    "amount": 85.50,
    "category": "Food",
    "type": "EXPENSE",
    "createdAt": "2026-05-23T15:30:50",
    "updatedAt": "2026-05-23T15:30:50"
}
```

## Running Tests

```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw test jacoco:report
```

Coverage report is generated at `target/site/jacoco/index.html`.

## CI/CD

Every push to `main` automatically:

1. Starts a PostgreSQL service container
2. Runs all 17 unit and integration tests
3. Generates a JaCoCo coverage report
4. Builds the application JAR
5. Validates the Docker image builds successfully

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
