# Library Lending System (Group 8) - Service A (Book Service)

This is a minimal Spring Boot microservice (Service A) for the course project.
It exposes REST endpoints for book data and reads initial data from `books.csv`.

- Port: 8081 (configured in `src/main/resources/application.properties`)
- Endpoints:
  - `GET /api/books` - list all books
  - `GET /api/books/{id}` - get book by id

Run with: `mvn spring-boot:run` inside the project root (requires Maven & Java 17).
