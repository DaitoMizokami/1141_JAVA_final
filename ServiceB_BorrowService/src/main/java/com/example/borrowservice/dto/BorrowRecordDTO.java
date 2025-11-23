package com.example.borrowservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class BorrowRecordDTO {
    private int bookId;
    private String borrower;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate borrowDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    public BorrowRecordDTO() {
    }

    public BorrowRecordDTO(int bookId, String borrower, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
