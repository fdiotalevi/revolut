package com.diotalevi.revolut.account.service;

public class OperationNotPermittedException extends Exception {

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
