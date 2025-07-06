package com.example.books_api.service;

import com.example.books_api.controller.dto.BookCreateRequest;
import com.example.books_api.model.Book;
import com.example.books_api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookCreateRequest request) {

        LocalDate buddhistDate = request.getPublishedDate();
        System.out.println("buddhistDate input: " + buddhistDate);

        if (buddhistDate.getYear() < 1000 || buddhistDate.isAfter(LocalDate.now().plusYears(543))) {
            throw new IllegalArgumentException("Published year must be valid (1000 <= year <= today in B.E.)");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublishedDate(buddhistDate);

        return bookRepository.save(book);
    }
}
