package com.example.library_api.repository;

import com.example.library_api.model.BorrowRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // Spring automatically writes the query based on this method's name —
    // this finds all borrow records where returnDate is still empty (not yet returned)
    List<BorrowRecord> findByReturnDateIsNull();

    // True if the book has any borrow history (used to block deleting it)
    boolean existsByBook_Id(Long bookId);

    // Loads the record and locks its row until the current transaction ends
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM BorrowRecord r WHERE r.id = :id")
    Optional<BorrowRecord> findByIdForUpdate(@Param("id") Long id);
}
