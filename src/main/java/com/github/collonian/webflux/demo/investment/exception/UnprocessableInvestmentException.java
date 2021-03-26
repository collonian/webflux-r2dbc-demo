package com.github.collonian.webflux.demo.investment.exception;

import com.github.collonian.webflux.demo.investment.InvestmentError;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnprocessableInvestmentException extends ResponseStatusException {
    InvestmentError errorCode;
    public InvestmentError getErrorCode() {
        return this.errorCode;
    }
    public UnprocessableInvestmentException(InvestmentError errorCode) {
        this(errorCode, null);
    }

    public UnprocessableInvestmentException(InvestmentError errorCode, String reason) {
        this(errorCode, reason, null);
    }

    public UnprocessableInvestmentException(InvestmentError errorCode, String reason, Throwable cause) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason, cause);
        this.errorCode = errorCode;
    }
}
