# books-api
## Requirements
- Java 17+
- Docker & Docker Compose
- Maven Wrapper (`./mvnw`)

---

## Setup Environment Variables
This project uses a `.env` file for local development
Follow these steps:

---

### Step 1: Copy the example file

Copy the `.env.example` to `.env`.

**For macOS/Linux:**
```bash
cp .env.example .env
```
**For Windows (PowerShell):**
```bash
Copy-Item .env.example .env
```

###  Step 2: Edit your .env file
Open your .env and fill in your local database connection:
```bash
DB_URL=jdbc:mysql://localhost:3306/booksdb?allowPublicKeyRetrieval=true&useSSL=false
DB_USERNAME=root
DB_PASSWORD=ascend
```

---

## Run MySQL with Docker
```bash
docker-compose up -d
```
## Database Setup

This project uses **MySQL**.

- The database `booksdb` will be automatically created by Docker Compose.
- The tables will be automatically created by Spring Boot Hibernate.
- No need for manual `schema.sql`.

>This is because `spring.jpa.hibernate.ddl-auto=update` is enabled.

---

## Run the Application

Use the Maven Wrapper to build and run the project:

```bash
./mvnw spring-boot:run
```

Or run the application directly from your IDE.

---
## Run Integration Tests

Run all integration tests with:

```bash
./mvnw test
```

Or run the application directly from your IDE.

---

## Example API Requests

### Create Book
#### Request
```http
POST /books
Content-Type: application/json

{
  "title": "Sample Book",
  "author": "Tester",
  "publishedDate": "2568-01-31"
}
```
#### Expected Response (200 OK)
```json
{
  "id": 1,
  "title": "Sample Book",
  "author": "Tester",
  "publishedDate": "2568-01-31"
}
```
---

### Get Books by Author
#### Request
```http
GET /books?author=Tester
```

#### Expected Response (200 OK)
```json
[
  {
    "id": 1,
    "title": "Sample Book",
    "author": "Tester",
    "publishedDate": "2568-01-31"
  }
]
```
---

## Development Workflow
- Always create a feature or ticket branch for your changes.
- Never push directly to `main` branch.
- Submit a Pull Request (PR) for review and comments.
- Avoid pushing directly to `main` branch.

**Note:** For this submission, all commits were pushed directly to `main` because I did not fully check the instructions before starting.