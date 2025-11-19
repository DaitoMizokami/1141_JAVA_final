package com.example.bookservice.repository;

import com.example.bookservice.model.Book;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private List<Book> bookList = new ArrayList<>();
    private final String FILE_PATH = "books.csv";

    public BookRepository() {
        loadBooksFromFile();
    }

    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
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

    public List<Book> findAll() { return bookList; }
    public Optional<Book> findById(int id) {
        return bookList.stream().filter(b -> b.getId() == id).findFirst();
    }
}
