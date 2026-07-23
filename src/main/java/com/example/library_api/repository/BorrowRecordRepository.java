package com.example.library_api.repository;

import com.example.library_api.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // Spring automatically writes the query based on this method's name —
    // this finds all borrow records where returnDate is still empty (not yet returned)
    List<BorrowRecord> findByReturnDateIsNull();
}