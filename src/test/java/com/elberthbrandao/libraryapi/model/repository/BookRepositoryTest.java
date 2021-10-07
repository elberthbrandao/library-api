package com.elberthbrandao.libraryapi.model.repository;

import com.elberthbrandao.libraryapi.api.dto.BookDTO;
import com.elberthbrandao.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("Test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado.")
    public void returnTrueWhenIsbnExists() {
        //cenário
        String isbn = "001";
        Book book = createNewBook(isbn);
        testEntityManager.persist(book);

        //execução
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verificações
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado.")
    public void returnTrueWhenIsbnNotExists() {
        //cenário
        String isbn = "001";

        //execução
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verificações
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest() {
        //cenário
        Book book = createNewBook("123");
        testEntityManager.persist(book);

        //execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        //verificações
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenário
        Book book = createNewBook("123");

        //execução
        Book savedBook = bookRepository.save(book);

        //verificações
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest() {
        //cenário
        Book book = createNewBook("123");
        testEntityManager.persist(book);
        Book foundBook = testEntityManager.find(Book.class, book.getId());

        //execução
        bookRepository.delete(foundBook);

        //verificações
        Book deletedBook = testEntityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }

    private Book createNewBook(String isbn) {
        return Book.builder().author("Artur").title("As aventuras").isbn(isbn).build();
    }
}