package com.example.borrowservice.model;

import java.time.LocalDate;

public class BorrowRecord {
    private int id;
    private int bookId;
    private String borrower;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public BorrowRecord() {
    }

    public BorrowRecord(int id, int bookId, String borrower, LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.bookId = bookId;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        LocalDate checkDate = returnDate != null ? returnDate : LocalDate.now();
        if (checkDate.isAfter(dueDate)) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, checkDate);
        }
        return 0;
    }
}
