package com.example.borrowservice.controller;

import com.example.borrowservice.dto.BorrowRecordDTO;
import com.example.borrowservice.exception.BorrowRecordNotFoundException;
import com.example.borrowservice.model.BorrowRecord;
import com.example.borrowservice.repository.BorrowRecordsRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://localhost:8081"}, allowCredentials = "true")
public class BorrowController {

    private final BorrowRecordsRepository repository;
    private final WebClient webClient;

    public BorrowController(BorrowRecordsRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
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

        // Verify book exists and is AVAILABLE via Book Service
        Map<String, Object> book;
        try {
            book = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/books/{id}").build(dto.getBookId()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } catch (WebClientResponseException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error contacting Book Service: " + ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error contacting Book Service: " + ex.getMessage());
        }

        if (book == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");

        String status = (String) book.getOrDefault("status", "");
        if (!"AVAILABLE".equals(status)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is not available for borrowing");
        }

        // mark book as CHECKED_OUT in Book Service via PATCH endpoint
        try {
            Map<String, String> statusUpdate = new HashMap<>();
            statusUpdate.put("status", "CHECKED_OUT");

            webClient.patch()
                    .uri(uriBuilder -> uriBuilder.path("/api/books/{id}/status").build(dto.getBookId()))
                    .bodyValue(statusUpdate)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update book status: " + ex.getMessage());
        }

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
        
        // First, get the borrow record to find the book ID
        var borrowRecord = repository.findById(id)
                .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found with id: " + id));
        
        // Mark as returned in borrow service
        boolean updated = repository.markAsReturned(id, date);
        if (updated) {
            // Now update the book status back to AVAILABLE in Book Service
            try {
                Map<String, String> statusUpdate = new HashMap<>();
                statusUpdate.put("status", "AVAILABLE");

                webClient.patch()
                        .uri(uriBuilder -> uriBuilder.path("/api/books/{id}/status").build(borrowRecord.getBookId()))
                        .bodyValue(statusUpdate)
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (Exception ex) {
                // Log but don't fail if book status update fails
                System.err.println("Warning: Failed to update book status to AVAILABLE: " + ex.getMessage());
            }
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
