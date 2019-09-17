package com.diotalevi.revolut.account.service;

import java.util.UUID;

public class EntityNotExistentException extends Exception {

    public EntityNotExistentException(UUID accountId) {
        super("Account doesn't exist: " + accountId.toString());
    }
}
