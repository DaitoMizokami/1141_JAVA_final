package com.example.borrowservice.controller;

import com.example.borrowservice.dto.BorrowRecordDTO;
import com.example.borrowservice.exception.BorrowRecordNotFoundException;
import com.example.borrowservice.model.BorrowRecord;
import com.example.borrowservice.repository.BorrowRecordsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://localhost:8081"}, allowCredentials = "true")
public class BorrowController {

    private final BorrowRecordsRepository repository;

    public BorrowController(BorrowRecordsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BorrowRecord> getAllBorrows() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public BorrowRecord getBorrowById(@PathVariable int id) {
        return repository.findById(id)
            .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found with id: " + id));
    }

    @GetMapping("/book/{bookId}")
    public List<BorrowRecord> getBorrowsByBookId(@PathVariable int bookId) {
        return repository.findByBookId(bookId);
    }

    @GetMapping("/status/overdue")
    public List<BorrowRecord> getOverdueBorrows() {
        return repository.findOverdue();
    }

    @PostMapping
    public ResponseEntity<BorrowRecord> createBorrow(@RequestBody BorrowRecordDTO dto) {
        validateBorrowRecordDTO(dto);

        BorrowRecord record = new BorrowRecord(
            0,
            dto.getBookId(),
            dto.getBorrower(),
            dto.getBorrowDate(),
            dto.getDueDate()
        );

        BorrowRecord created = repository.save(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowRecord> updateBorrow(@PathVariable int id, @RequestBody BorrowRecordDTO dto) {
        validateBorrowRecordDTO(dto);

        BorrowRecord updated = new BorrowRecord(
            id,
            dto.getBookId(),
            dto.getBorrower(),
            dto.getBorrowDate(),
            dto.getDueDate()
        );

        return repository.update(id, updated)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found with id: " + id));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int id, @RequestParam(required = false) String returnDate) {
        LocalDate date = returnDate != null ? LocalDate.parse(returnDate) : LocalDate.now();
        
        boolean updated = repository.markAsReturned(id, date);
        if (updated) {
            return ResponseEntity.noContent().build();
        } else {
            throw new BorrowRecordNotFoundException("Borrow record not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable int id) {
        boolean removed = repository.delete(id);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            throw new BorrowRecordNotFoundException("Borrow record not found with id: " + id);
        }
    }

    private void validateBorrowRecordDTO(BorrowRecordDTO dto) {
        if (dto.getBookId() <= 0) {
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        if (dto.getBorrower() == null || dto.getBorrower().trim().isEmpty()) {
            throw new IllegalArgumentException("Borrower name is required");
        }
        if (dto.getBorrowDate() == null) {
            throw new IllegalArgumentException("Borrow date is required");
        }
        if (dto.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }
        if (dto.getDueDate().isBefore(dto.getBorrowDate())) {
            throw new IllegalArgumentException("Due date must be after borrow date");
        }
    }
}
