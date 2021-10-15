package com.elberthbrandao.libraryapi.model.repository;

import com.elberthbrandao.libraryapi.model.entity.Book;
import com.elberthbrandao.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.elberthbrandao.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("Test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe um empréstimo não devolvido para o livro.")
    public void existsByBookAndNotReturnedTest() {
        //Cenário
        Loan loan = createAndPersistLoan();

        //Execução
        boolean exists = loanRepository.existsByBookAndNotReturned(loan.getBook());

        //Verificações
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar um empréstimo pelo isbn do livro ou customer.")
    public void findByBookIsbnOrCustomer() {
        Loan loan = createAndPersistLoan();

        Page<Loan> result = loanRepository.findByBookIsbnOrCustomer(
                loan.getBook().getIsbn(), loan.getCustomer(), PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    public Loan createAndPersistLoan(){
        Book book = createNewBook("123");

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();

        entityManager.persist(book);
        entityManager.persist(loan);

        return loan;
    }
}
