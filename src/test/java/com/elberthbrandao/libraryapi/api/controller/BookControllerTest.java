package com.elberthbrandao.libraryapi.api.controller;

import com.elberthbrandao.libraryapi.api.dto.BookDTO;
import com.elberthbrandao.libraryapi.entity.Book;
import com.elberthbrandao.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {
        BookDTO bookDTO = BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
        Book savedBook = Book.builder().id(1L).author("Artur").title("As aventuras").isbn("001").build();

        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = objectMapper.writeValueAsString(bookDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value(bookDTO.getTitle()))
                .andExpect(jsonPath("author").value(bookDTO.getAuthor()))
                .andExpect(jsonPath("isbn").value(bookDTO.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para a criação do livro.")
    public void createInvalidBookTest() {

    }


}
