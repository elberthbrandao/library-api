package com.elberthbrandao.libraryapi.model.repository;

import com.elberthbrandao.libraryapi.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
