# Library Management REST API

A REST API for managing a library system — books, members, and borrow/return records — built with Java and Spring Boot.

## Features
- Full CRUD operations for books
- Member registration
- Borrow and return tracking with relational data (Book ↔ Member ↔ BorrowRecord)
- View all currently active (unreturned) borrows

## Tech Stack
- Java 17
- Spring Boot (Spring Web, Spring Data JPA)
- H2 Database (file-based)
- Gradle
- Tested with Postman

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /books | List all books |
| GET | /books/{id} | Get a single book |
| POST | /books | Add a new book |
| PUT | /books/{id} | Update a book |
| DELETE | /books/{id} | Delete a book |
| GET | /members | List all members |
| POST | /members | Register a new member |
| POST | /borrow | Borrow a book |
| PUT | /return/{recordId} | Return a borrowed book |
| GET | /active-borrows | List all currently borrowed (unreturned) books |

## Sample Request

**POST** `/books`
```json
{
  "title": "1984",
  "author": "George Orwell"
}
```

**POST** `/borrow`
```json
{
  "bookId": 1,
  "memberId": 1
}
```

## How to Run

1. Clone the repository
```bash
git clone https://github.com/khushisaharann/library-management-api.git
```
2. Run using the Gradle wrapper
```bash
./gradlew bootRun
```
3. Server starts at `http://localhost:8080`

## What I Learned
This project helped me understand REST API design, relational data modeling, and how Spring Boot connects controllers, repositories, and a database using JPA — alongside my primary focus in cybersecurity.