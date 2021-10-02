package com.elberthbrandao.libraryapi.service.impl;

import com.elberthbrandao.libraryapi.exception.BusinessException;
import com.elberthbrandao.libraryapi.model.entity.Book;
import com.elberthbrandao.libraryapi.model.repository.BookRepository;
import com.elberthbrandao.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl (BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        if(bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado.");
        }
        return bookRepository.save(book);
    }
}
