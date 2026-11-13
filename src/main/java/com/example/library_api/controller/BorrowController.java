package com.example.library_api.controller;

import com.example.library_api.model.BorrowRecord;
import com.example.library_api.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrow")
    public BorrowRecord borrowBook(@RequestBody Map<String, Long> payload) {
        return borrowService.borrowBook(payload.get("bookId"), payload.get("memberId"));
    }

    @PutMapping("/return/{recordId}")
    public BorrowRecord returnBook(@PathVariable Long recordId) {
        return borrowService.returnBook(recordId);
    }

    @GetMapping("/active-borrows")
    public List<BorrowRecord> getActiveBorrows() {
        return borrowService.getActiveBorrows();
    }
}
