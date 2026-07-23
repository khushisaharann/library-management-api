package com.example.library_api.controller;

import com.example.library_api.model.Book;
import com.example.library_api.model.BorrowRecord;
import com.example.library_api.repository.BookRepository;
import com.example.library_api.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class BorrowController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody Map<String, Long> payload) {
        Long bookId = payload.get("bookId");
        Long memberId = payload.get("memberId");

        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return ResponseEntity.badRequest().body("Book not found");
        }
        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body("Book is not available");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord(bookId, memberId);
        borrowRecordRepository.save(record);

        return ResponseEntity.ok(record);
    }

    @PutMapping("/return/{recordId}")
    public ResponseEntity<?> returnBook(@PathVariable Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId).orElse(null);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        Book book = bookRepository.findById(record.getBookId()).orElse(null);
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/active-borrows")
    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository.findByReturnDateIsNull();
    }
}