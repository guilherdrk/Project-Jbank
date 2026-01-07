package com.guilherdrk.jbank.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

public class WalletDataAlreadyExistsException extends JBankException{

    private final String detail;

    public WalletDataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("Wallet data already exists");
        pd.setDetail(detail);

        return pd;
    }
}
