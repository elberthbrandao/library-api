package com.elberthbrandao.libraryapi.api.controller;

import com.elberthbrandao.libraryapi.api.dto.BookDTO;
import com.elberthbrandao.libraryapi.entity.Book;
import com.elberthbrandao.libraryapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO bookDTO) {
        Book entity = Book.builder()
                .author(bookDTO.getAuthor())
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .build();

        entity = bookService.save(entity);

        BookDTO dto = BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .build();

        return dto;
    }

}
