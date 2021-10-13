package com.elberthbrandao.libraryapi.service.impl;

import com.elberthbrandao.libraryapi.exception.BusinessException;
import com.elberthbrandao.libraryapi.model.entity.Loan;
import com.elberthbrandao.libraryapi.model.repository.LoanRepository;
import com.elberthbrandao.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if(loanRepository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Livro já emprestado.");
        }
        return loanRepository.save(loan);
    }
}
