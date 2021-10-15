package com.elberthbrandao.libraryapi.service;

import com.elberthbrandao.libraryapi.api.dto.LoanFilterDTO;
import com.elberthbrandao.libraryapi.exception.BusinessException;
import com.elberthbrandao.libraryapi.model.entity.Book;
import com.elberthbrandao.libraryapi.model.entity.Loan;
import com.elberthbrandao.libraryapi.model.repository.BookRepository;
import com.elberthbrandao.libraryapi.model.repository.LoanRepository;
import com.elberthbrandao.libraryapi.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService loanService;

    @MockBean
    LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        this.loanService = new LoanServiceImpl(loanRepository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo.")
    public void saveLoanTest() {
        Book book = Book.builder().id(1L).build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .id(1L)
                .book(book)
                .customer("Fulano")
                .loanDate(LocalDate.now())
                .build();

        when(loanRepository.existsByBookAndNotReturned(savingLoan.getBook())).thenReturn(false);
        when(loanRepository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = loanService.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um empréstimo de um livro já emprestado.")
    public void loanedBookSaveTest() {
        Book book = Book.builder().id(1L).build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(LocalDate.now())
                .build();

        when(loanRepository.existsByBookAndNotReturned(savingLoan.getBook())).thenReturn(true);

        Throwable exception = catchThrowable(() -> loanService.save(savingLoan));

        assertThat(exception).isInstanceOf(BusinessException.class)
                .hasMessage("Livro já emprestado.");

        verify(loanRepository, never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo ID")
    public void getLoanDetailsTest() {
        //Cenário
        long id = 1L;
        Loan loan = createLoan();
        loan.setId(id);

        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        //Execução
        Optional<Loan> foundedLoan = loanService.getById(id);

        //Verificação
        assertThat(foundedLoan.isPresent()).isTrue();
        assertThat(foundedLoan.get().getId()).isEqualTo(loan.getId());
        assertThat(foundedLoan.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(foundedLoan.get().getBook()).isEqualTo(loan.getBook());
        assertThat(foundedLoan.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(loanRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo")
    public void updateTest() {
        //Cenário
        long id = 1L;
        Loan loan = createLoan();
        loan.setId(id);
        loan.setReturned(true);

        when(loanRepository.save(loan)).thenReturn(loan);

        Loan updatedLoan = loanService.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades.")
    public void findLoanTest() {
        //cenário
        Loan loan = createLoan();
        loan.setId(1L);
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Fulano").isbn("321").build();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = List.of(loan);

        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());

        when(loanRepository.findByBookIsbnOrCustomer(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(PageRequest.class))
        ).thenReturn(page);

        //execução
        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        //verificações
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1L).build();

        return Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(LocalDate.now())
                .build();
    }
}
