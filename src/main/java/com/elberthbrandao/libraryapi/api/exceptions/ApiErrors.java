package com.elberthbrandao.libraryapi.api.exceptions;

import com.elberthbrandao.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {
    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BusinessException businessException) {
        this.errors = List.of(businessException.getMessage());
    }

    public ApiErrors(ResponseStatusException responseStatusException) {
        this.errors = List.of(responseStatusException.getReason());
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
