package com.elberthbrandao.libraryapi.model.repository;

import com.elberthbrandao.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
