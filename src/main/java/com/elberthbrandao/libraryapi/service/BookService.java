package com.elberthbrandao.libraryapi.service;

import com.elberthbrandao.libraryapi.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book);
}
