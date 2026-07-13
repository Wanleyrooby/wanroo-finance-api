package com.wanroo.finance.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException() {
        super("Transação não encontrada.");
    }
}
