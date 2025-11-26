# Library Lending System (Group 8)

## Project Overview
This is the final project for the **1141 Java Course**.
We are building a microservice-based application using Spring Boot.

## Team Members & Roles
* **Service A (Book Service):** 
    * Daito Mizokami - Project setup and architecture
    * Lin Yuting - CRUD operations, REST API endpoints, and frontend
    * Manages book inventory and data.
    * Port: `8081`
* **Service B (Borrow Service):**
    * Hong fuyan – Controller, Request DTOs, pom.xml
    * Lin Yuting - Complete borrow management system, 8 REST API endpoints, WebClient integration, and frontend
    * Manages borrowing logic and user transactions.
    * Port: `8080`

## Microservice Architecture & Service Integration

### How the Services Work Together

**Service A ↔ Service B Communication via Spring WebClient:**

1. **Creating a Borrow Record:**
   - User selects book in borrow.html
   - Service B receives POST request to create borrow
   - Service B uses WebClient to call `GET /api/books/{id}` on Service A
   - Verifies book exists and status is AVAILABLE
   - If available, calls `PATCH /api/books/{id}/status` to update status to CHECKED_OUT
   - Creates borrow record in Service B's database

2. **Returning a Book:**
   - User clicks "還書" (Return) button in borrow.html
   - Service B marks record as returned
   - Service B automatically calls `PATCH /api/books/{id}/status` on Service A
   - Book status changes back to AVAILABLE in Service A
   - User sees updated availability in book management

3. **Frontend Integration (borrow.html):**
   - Dynamically loads all books from Service A on page load
   - Book dropdown shows status (可借 = available, 已借出 = checked out)
   - Displays full book details (title, author, status) when selected
   - Shows book titles in borrow records table (not just IDs)
   - All updates automatically sync between services

## Quick Start

### Requirements
- **Java:** 17 or higher
- **Maven:** 3.9.11 or higher
- **macOS/Linux/Windows** with zsh/bash

## How to Run This Project

### Step 1: Build Both Services

Open a terminal in the project root directory and build both services:

```bash
# Build Service A (Book Service)
cd ServiceA_BookService
mvn -DskipTests clean package
cd ..

# Build Service B (Borrow Service)
cd ServiceB_BorrowService
mvn -DskipTests clean package
cd ..
```

### Step 2: Start Both Services

You need two separate terminal windows.

Terminal Window 1 - Start Service A:

```bash
cd ServiceA_BookService
java -jar target/book-service-0.0.1-SNAPSHOT.jar
```

Wait until you see: "Tomcat started on port(s): 8081"

Terminal Window 2 - Start Service B:

```bash
cd ServiceB_BorrowService
java -jar target/borrow-service-0.0.1-SNAPSHOT.jar
```

Wait until you see: "Tomcat started on port(s): 8080"

### Step 3: Access the Application

Open your web browser and visit:

- Home Page: http://localhost:8081/index.html
- Book Management: http://localhost:8081/books.html
- Borrow Management: http://localhost:8080/borrow.html

Or test API endpoints:
Or test API endpoints:

```bash
# Get all books
curl http://localhost:8081/api/books

# Get all borrow records
curl http://localhost:8080/api/borrows
```

### Step 4: Stop the Services

To stop the services, press Ctrl+C in each terminal window.

Or use this command to stop all Java services:

```bash
pkill -f "java -jar"
```

## Service A (Book Service) - Port 8081

### API Endpoints

GET /api/books - Get all books

```bash
curl http://localhost:8081/api/books
```

GET /api/books/1 - Get book by ID

```bash
curl http://localhost:8081/api/books/1
```

POST /api/books - Create a new book

```bash
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Java Programming","author":"John Doe","status":"AVAILABLE"}'
```

PUT /api/books/1 - Update a book

```bash
curl -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","author":"New Author","status":"CHECKED_OUT"}'
```

DELETE /api/books/1 - Delete a book

```bash
curl -X DELETE http://localhost:8081/api/books/1
```

PATCH /api/books/1/status - Update book status (used by Service B via WebClient)

```bash
curl -X PATCH http://localhost:8081/api/books/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"CHECKED_OUT"}'
```

Note: This endpoint is primarily used by Service B to update book status when borrowing or returning books.

## Service B (Borrow Service) - Port 8080

### API Endpoints

GET /api/borrows - Get all borrow records

```bash
curl http://localhost:8080/api/borrows
```

GET /api/borrows/1 - Get borrow record by ID

```bash
curl http://localhost:8080/api/borrows/1
```

GET /api/borrows/book/1 - Get records by book ID

```bash
curl http://localhost:8080/api/borrows/book/1
```

GET /api/borrows/status/overdue - Get overdue records

```bash
curl http://localhost:8080/api/borrows/status/overdue
```

POST /api/borrows - Create a borrow record

```bash
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"borrower":"Alice","borrowDate":"2025-11-23","dueDate":"2025-12-07"}'
```

PUT /api/borrows/1 - Update a borrow record

```bash
curl -X PUT http://localhost:8080/api/borrows/1 \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"borrower":"Alice Chen","borrowDate":"2025-11-23","dueDate":"2025-12-10"}'
```

PUT /api/borrows/1/return - Mark book as returned

```bash
curl -X PUT "http://localhost:8080/api/borrows/1/return?returnDate=2025-11-25"
```

DELETE /api/borrows/1 - Delete a borrow record

```bash
curl -X DELETE http://localhost:8080/api/borrows/1
```

## Project Structure

ServiceA_BookService/
- books.csv (Book database)
- pom.xml (Maven configuration)
- src/main/java/com/example/bookservice/
  - BookServiceApplication.java (Main application class)
  - controller/BookController.java (REST API endpoints)
  - model/Book.java (Book model)
  - model/LibraryItem.java (Library item interface)
  - repository/BookRepository.java (Data persistence)
  - exception/BookNotFoundException.java (Custom exception)
- src/main/resources/
  - application.properties (Configuration)
  - static/index.html (Home page)
  - static/books.html (Book management UI)

ServiceB_BorrowService/
- pom.xml (Maven configuration)
- src/main/java/com/example/borrowservice/
  - BorrowServiceApplication.java (Main application class)
  - controller/BorrowController.java (REST API endpoints)
  - model/BorrowRecord.java (Borrow record model)
  - dto/BorrowRecordDTO.java (Data transfer object)
  - repository/BorrowRecordsRepository.java (Data persistence)
  - exception/BorrowRecordNotFoundException.java (Custom exception)
- src/main/resources/
  - application.properties (Configuration)
  - static/borrow.html (Borrow management UI)

## Features

Book Service (Service A):
- Create, read, update, and delete books
- Book status tracking (AVAILABLE or CHECKED_OUT)
- CSV data persistence
- Clean user interface
- REST API with 6 endpoints:
  - GET /api/books (get all books)
  - GET /api/books/{id} (get book by ID)
  - POST /api/books (create book)
  - PUT /api/books/{id} (update book)
  - DELETE /api/books/{id} (delete book)
  - **PATCH /api/books/{id}/status** (update status only - used by Service B)

Borrow Service (Service B) - **Integrated with Service A via WebClient:**
- Create, read, update, and delete borrow records
- REST API with 8 endpoints:
  - GET /api/borrows (get all records)
  - GET /api/borrows/{id} (get by ID)
  - GET /api/borrows/book/{bookId} (get records by book)
  - GET /api/borrows/status/overdue (get overdue records)
  - POST /api/borrows (create borrow - validates availability, updates book status)
  - PUT /api/borrows/{id} (update record)
  - **PUT /api/borrows/{id}/return** (mark returned - automatically updates book status to AVAILABLE)
  - DELETE /api/borrows/{id} (delete record)
- **Service-to-Service Communication:**
  - Validates book availability before borrowing via `GET /api/books/{id}` on Service A
  - Updates book status to CHECKED_OUT when borrowing via `PATCH /api/books/{id}/status` on Service A
  - Updates book status to AVAILABLE when returning book via `PATCH /api/books/{id}/status` on Service A
  - All status changes propagate automatically between services
- **Frontend Features:**
  - Dynamic book dropdown (loads from Service A)
  - Shows book availability status (可借 / 已借出)
  - Displays book details when selected
  - Shows book titles in borrow table
  - Statistics dashboard (total, borrowed, returned, overdue)
- Track overdue books with automatic calculation
- CSV data persistence

## Technology Stack

- Framework: Spring Boot 3.1.4
- Language: Java 17
- Build Tool: Maven 3.9.11
- Server: Apache Tomcat 10.1.13
- Frontend: HTML5, CSS3, Vanilla JavaScript
- Data Format: JSON (API), CSV (storage)
