package com.example.bookservice.model;

public class Book extends LibraryItem {
    private String author;
    private String status; // AVAILABLE or CHECKED_OUT

    // no-arg constructor for deserialization
    public Book() { super(); }

    public Book(int id, String title, String author, String status) {
        super(id, title);
        this.author = author;
        this.status = status;
    }

    public String getAuthor() { return author; }
    public String getStatus() { return status; }
    public void setAuthor(String author) { this.author = author; }
    public void setStatus(String status) { this.status = status; }
}
