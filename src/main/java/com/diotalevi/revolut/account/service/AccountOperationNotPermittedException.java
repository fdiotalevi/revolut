package com.diotalevi.revolut.account.service;

public class AccountOperationNotPermittedException extends Exception {

    public AccountOperationNotPermittedException(String message) {
        super(message);
    }
}
