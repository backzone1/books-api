package com.example.books_api.controller;

import com.example.books_api.controller.dto.BookCreateRequest;
import com.example.books_api.model.Book;
import com.example.books_api.repository.BookRepository;
import com.example.books_api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public BookController(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookRepository.findByAuthor(author);
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody @Valid BookCreateRequest request) {
        return bookService.createBook(request);
    }
}