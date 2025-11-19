package com.example.bookservice.controller;

import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://localhost:8081"}, allowCredentials = "true")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book created = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {
        return bookRepository.update(id, book)
                .map(b -> ResponseEntity.ok(b))
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        boolean removed = bookRepository.delete(id);
        if (!removed) throw new BookNotFoundException("Book not found with id: " + id);
        return ResponseEntity.noContent().build();
    }
}
