package com.example.library_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private Long memberId;
    private LocalDateTime borrowDate = LocalDateTime.now();
    private LocalDateTime returnDate; // stays empty (null) until the book is returned

    public BorrowRecord() {}

    public BorrowRecord(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public LocalDateTime getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
}