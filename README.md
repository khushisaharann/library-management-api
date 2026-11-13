# Library Management REST API

A REST API for managing a library system — books, members, and borrow/return records — built with Java and Spring Boot.

## Features
- Three relational entities: Book, Member, BorrowRecord (BorrowRecord → Book and Member via `@ManyToOne`)
- CRUD operations for books, plus member registration
- Borrow/return logic with availability tracking to prevent double-borrowing
- View all currently active (unreturned) borrows

## Architecture
Layered structure separating data, persistence, and business logic:
- **Controller** (`controller/`) – handles HTTP requests and responses
- **Service** (`service/`) – business logic (availability checks, borrow/return rules)
- **Repository** (`repository/`) – data access with Spring Data JPA
- **Model** (`model/`) – JPA entities

## Borrow / Return Rules
- A book can only be borrowed while it is available; borrowing marks it unavailable.
- Each borrow/return runs in one transaction with a row lock on the book, so two simultaneous requests can't borrow the same book.
- A borrow record can only be returned once; returning marks the book available again.
- Error responses: `404` (book/member/record not found), `409` (already borrowed, already returned, or deleting a book with borrow records), `400` (missing ids).

## Tech Stack
- Java 21
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