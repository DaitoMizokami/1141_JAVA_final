package com.example.borrowservice.repository;

import com.example.borrowservice.model.BorrowRecord;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BorrowRecordsRepository {
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
    private final Path FILE_PATH;

    public BorrowRecordsRepository() {
        FILE_PATH = resolveCsvPath();
        loadBorrowRecordsFromFile();
    }

    private void loadBorrowRecordsFromFile() {
        try (BufferedReader br = Files.newBufferedReader(FILE_PATH)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    int id = Integer.parseInt(data[0].trim());
                    int bookId = Integer.parseInt(data[1].trim());
                    String borrower = unescapeCsv(data[2].trim());
                    LocalDate borrowDate = LocalDate.parse(data[3].trim());
                    LocalDate dueDate = LocalDate.parse(data[4].trim());
                    
                    BorrowRecord record = new BorrowRecord(id, bookId, borrower, borrowDate, dueDate);
                    
                    if (!data[5].trim().equals("null")) {
                        record.setReturnDate(LocalDate.parse(data[5].trim()));
                    }
                    
                    borrowRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Borrow records CSV not found. Starting with empty repository.");
        }
    }

    private void writeBorrowRecordsToFile() {
        try (java.io.BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
            for (BorrowRecord record : borrowRecords) {
                String line = String.format("%d,%d,%s,%s,%s,%s%n",
                    record.getId(),
                    record.getBookId(),
                    escapeCsv(record.getBorrower()),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    record.getReturnDate() != null ? record.getReturnDate() : "null"
                );
                writer.write(line);
            }
        } catch (IOException e) {
            System.out.println("CSV Write Error: " + e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCsv(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    private Path resolveCsvPath() {
        Path path = Paths.get("borrow_records.csv");
        if (Files.exists(path)) {
            return path;
        }
        path = Paths.get("ServiceB_BorrowService", "borrow_records.csv");
        if (Files.exists(path)) {
            return path;
        }
        path = Paths.get("..", "borrow_records.csv").normalize();
        if (Files.exists(path)) {
            return path;
        }
        return Paths.get("borrow_records.csv");
    }

    public List<BorrowRecord> findAll() {
        return new ArrayList<>(borrowRecords);
    }

    public Optional<BorrowRecord> findById(int id) {
        return borrowRecords.stream()
            .filter(r -> r.getId() == id)
            .findFirst();
    }

    public List<BorrowRecord> findByBookId(int bookId) {
        return borrowRecords.stream()
            .filter(r -> r.getBookId() == bookId)
            .collect(Collectors.toList());
    }

    public List<BorrowRecord> findOverdue() {
        return borrowRecords.stream()
            .filter(r -> r.getReturnDate() == null && r.isOverdue())
            .collect(Collectors.toList());
    }

    public BorrowRecord save(BorrowRecord record) {
        int maxId = borrowRecords.stream()
            .map(BorrowRecord::getId)
            .max(Integer::compareTo)
            .orElse(0);
        
        int newId = maxId + 1;
        record.setId(newId);
        borrowRecords.add(record);
        writeBorrowRecordsToFile();
        return record;
    }

    public Optional<BorrowRecord> update(int id, BorrowRecord updated) {
        return findById(id).flatMap(existing -> {
            existing.setBookId(updated.getBookId());
            existing.setBorrower(updated.getBorrower());
            existing.setBorrowDate(updated.getBorrowDate());
            existing.setDueDate(updated.getDueDate());
            existing.setReturnDate(updated.getReturnDate());
            writeBorrowRecordsToFile();
            return Optional.of(existing);
        });
    }

    public boolean delete(int id) {
        boolean removed = borrowRecords.removeIf(r -> r.getId() == id);
        if (removed) {
            writeBorrowRecordsToFile();
        }
        return removed;
    }

    public boolean markAsReturned(int id, LocalDate returnDate) {
        Optional<BorrowRecord> found = findById(id);
        if (found.isPresent()) {
            BorrowRecord record = found.get();
            record.setReturnDate(returnDate);
            writeBorrowRecordsToFile();
            return true;
        }
        return false;
    }
}
