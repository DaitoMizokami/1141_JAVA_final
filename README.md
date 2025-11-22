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
    * Manages borrowing logic and user transactions.
    * Port: `8080`

## Quick Start

### Requirements
- **Java:** 17 or higher
- **Maven:** 3.9.11 or higher
- **macOS/Linux/Windows** with zsh/bash

### Running Service A (Book Service)

#### Method 1: Build with Maven and Run (Recommended)

```bash
cd ServiceA_BookService
mvn -DskipTests clean package
java -jar target/book-service-0.0.1-SNAPSHOT.jar &
sleep 3
curl http://localhost:8081/api/books
```

#### Method 2: Run Directly with Maven

```bash
cd ServiceA_BookService
mvn spring-boot:run
```

### Accessing the Application

Open your browser and navigate to:

- **Landing Page:** `http://localhost:8081/`
- **Book Management System:** `http://localhost:8081/books.html`
- **REST API (Get All Books):** `http://localhost:8081/api/books`

## REST API Endpoints

### Get All Books
```bash
curl http://localhost:8081/api/books
```
Returns a JSON array of all books.

### Get Book by ID
```bash
curl http://localhost:8081/api/books/1
```
Returns the book with the specified ID.

### Create a Book
```bash
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Java Programming","author":"John Doe","status":"AVAILABLE"}'
```

### Update a Book
```bash
curl -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","author":"New Author","status":"CHECKED_OUT"}'
```

### Delete a Book
```bash
curl -X DELETE http://localhost:8081/api/books/1
```

### Stopping the Service

```bash
pkill -f "java -jar"
```

Or by PID:
```bash
ps aux | grep "java -jar"
kill <PID>
```

## Project Structure

```
ServiceA_BookService/
├── books.csv                          (Book database)
├── pom.xml                            (Maven configuration)
├── src/
│   ├── main/
│   │   ├── java/com/example/bookservice/
│   │   │   ├── BookServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   └── BookController.java          (REST API endpoints)
│   │   │   ├── model/
│   │   │   │   ├── Book.java                    (Book model)
│   │   │   │   └── LibraryItem.java             (Library item)
│   │   │   ├── repository/
│   │   │   │   └── BookRepository.java          (Data persistence layer)
│   │   │   └── exception/
│   │   │       └── BookNotFoundException.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html                   (Landing page)
│   │           └── books.html                   (Book management system)
│   └── test/
└── target/
    └── book-service-0.0.1-SNAPSHOT.jar         (Executable JAR)
```

## Features

- **Book Management:** Create, read, update, and delete books
- **MUJI-style Frontend:** Clean, minimalist user interface
- **REST API:** Standard HTTP methods for CRUD operations
- **CSV Persistence:** Book data stored in books.csv
- **CORS Support:** Cross-origin resource sharing enabled

## Technology Stack

- **Framework:** Spring Boot 3.1.4
- **Server:** Apache Tomcat 10.1.13
- **Build Tool:** Maven 3.9.11
- **Language:** Java 17
- **Frontend:** HTML5 + Vanilla JavaScript + CSS3
- **Data Format:** JSON (API), CSV (persistence)
