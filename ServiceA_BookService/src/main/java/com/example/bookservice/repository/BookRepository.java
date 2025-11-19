package com.example.bookservice.repository;

import com.example.bookservice.model.Book;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private List<Book> bookList = new ArrayList<>();
    // try a few candidate locations for the CSV so running from different working dirs works
    private final Path FILE_PATH = resolveCsvPath();

    public BookRepository() {
        loadBooksFromFile();
    }

    private void loadBooksFromFile() {
        if (FILE_PATH == null) {
            System.out.println("books.csv not found in expected locations. Starting with empty repository.");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(FILE_PATH)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    int id = Integer.parseInt(data[0].trim());
                    bookList.add(new Book(id, data[1].trim(), data[2].trim(), data[3].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("CSV Read Error: " + e.getMessage());
        }
    }

    private static Path resolveCsvPath() {
        // candidates relative to current working directory
        Path p1 = Paths.get("books.csv");
        if (Files.exists(p1)) return p1;

        Path p2 = Paths.get("ServiceA_BookService", "books.csv");
        if (Files.exists(p2)) return p2;

        // try project root (one level up)
        Path p3 = Paths.get("..", "ServiceA_BookService", "books.csv").normalize();
        if (Files.exists(p3)) return p3;

        return null;
    }

    private void writeBooksToFile() {
        if (FILE_PATH == null) {
            System.out.println("books.csv not found; cannot persist changes.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(FILE_PATH))) {
            for (Book b : bookList) {
                // id,title,author,status
                pw.printf("%d,%s,%s,%s\n", b.getId(), escapeCsv(b.getTitle()), escapeCsv(b.getAuthor()), escapeCsv(b.getStatus()));
            }
        } catch (IOException e) {
            System.out.println("CSV Write Error: " + e.getMessage());
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        // very small escape: wrap in quotes if contains comma or quote
        if (s.contains(",") || s.contains("\"")) {
            return '"' + s.replace("\"", "\"\"") + '"';
        }
        return s;
    }

    public List<Book> findAll() { return bookList; }
    public Optional<Book> findById(int id) {
        return bookList.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Book save(Book book) {
        // assign id if missing or zero
        int newId = book.getId();
        if (newId <= 0) {
            Optional<Integer> maxId = bookList.stream().map(Book::getId).max(Comparator.naturalOrder());
            newId = maxId.map(i -> i + 1).orElse(1);
        }
        Book toAdd = new Book(newId, book.getTitle(), book.getAuthor(), book.getStatus());
        bookList.add(toAdd);
        writeBooksToFile();
        return toAdd;
    }

    public Optional<Book> update(int id, Book updated) {
        Optional<Book> found = findById(id);
        found.ifPresent(b -> {
            if (updated.getTitle() != null) b.setTitle(updated.getTitle());
            if (updated.getAuthor() != null) b.setAuthor(updated.getAuthor());
            if (updated.getStatus() != null) b.setStatus(updated.getStatus());
            writeBooksToFile();
        });
        return found;
    }

    public boolean delete(int id) {
        boolean removed = bookList.removeIf(b -> b.getId() == id);
        if (removed) writeBooksToFile();
        return removed;
    }
}
