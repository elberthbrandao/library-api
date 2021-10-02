package com.elberthbrandao.libraryapi.service;

import com.elberthbrandao.libraryapi.exception.BusinessException;
import com.elberthbrandao.libraryapi.model.entity.Book;
import com.elberthbrandao.libraryapi.model.repository.BookRepository;
import com.elberthbrandao.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    public void saveBookTest() {
        //cenário
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(bookRepository.save(book)).thenReturn(
                Book.builder()
                        .id(1L)
                        .isbn(book.getIsbn())
                        .author(book.getAuthor())
                        .title(book.getTitle())
                        .build()
        );

        //execução
        Book savedBook = bookService.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("Deve lançar um erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN () {
        //cenário
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        //verificações
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(bookRepository, Mockito.never()).save(book);

    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }
}
