package com.elberthbrandao.libraryapi.api.controller;

import com.elberthbrandao.libraryapi.api.dto.BookDTO;
import com.elberthbrandao.libraryapi.api.exceptions.ApiErrors;
import com.elberthbrandao.libraryapi.exception.BusinessException;
import com.elberthbrandao.libraryapi.model.entity.Book;
import com.elberthbrandao.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO bookDTO) {
        Book entity = modelMapper.map(bookDTO, Book.class);
        entity = bookService.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id) {
        return bookService
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest) {
       Book filter = modelMapper.map(bookDTO, Book.class);
       Page<Book> result = bookService.find(filter, pageRequest);
       List<BookDTO> list = result.getContent()
            .stream()
            .map(entity -> modelMapper.map(entity, BookDTO.class))
            .collect(Collectors.toList());

       return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
        return bookService.getById(id).map( book -> {
            book.setAuthor(bookDTO.getAuthor());
            book.setTitle(bookDTO.getTitle());
            book = bookService.update(book);
            return modelMapper.map(book, BookDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book book = bookService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bookService.delete(book);
    }
}
