package com.example.library_api.service;

import com.example.library_api.model.Book;
import com.example.library_api.repository.BookRepository;
import com.example.library_api.repository.BorrowRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BookService(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    public Book addBook(Book book) {
        // Availability is managed only by borrow/return
        book.setId(null);
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        // Lock the row so this edit can't overwrite a borrow/return happening at the same time
        Book book = bookRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        // Keeps relational integrity: borrow records must always point to an existing book
        if (borrowRecordRepository.existsByBook_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book has borrow records and can't be deleted");
        }
        bookRepository.delete(book);
    }
}
