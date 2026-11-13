package com.example.library_api.service;

import com.example.library_api.model.Book;
import com.example.library_api.model.BorrowRecord;
import com.example.library_api.model.Member;
import com.example.library_api.repository.BookRepository;
import com.example.library_api.repository.BorrowRecordRepository;
import com.example.library_api.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Borrow / return business logic with availability tracking.
 *
 * Double-borrowing is prevented by:
 *  - running each borrow/return as one transaction, and
 *  - locking the book's row while its availability is checked and changed,
 *    so two simultaneous requests for the same book are handled one after the other.
 */
@Service
public class BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(BookRepository bookRepository,
                         MemberRepository memberRepository,
                         BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BorrowRecord borrowBook(Long bookId, Long memberId) {
        if (bookId == null || memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookId and memberId are required");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (!book.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is already borrowed");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        return borrowRecordRepository.save(new BorrowRecord(book, member));
    }

    @Transactional
    public BorrowRecord returnBook(Long recordId) {
        // Lock the record so the same record can't be returned twice at the same time
        BorrowRecord record = borrowRecordRepository.findByIdForUpdate(recordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrow record not found"));

        if (record.getReturnDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book has already been returned");
        }

        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        Book book = bookRepository.findByIdForUpdate(record.getBook().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        book.setAvailable(true);
        bookRepository.save(book);

        return record;
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository.findByReturnDateIsNull();
    }
}
