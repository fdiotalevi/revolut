package com.diotalevi.revolut.account.service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Account {

    private UUID id;
    private String owner;
    private AtomicLong balanceCents;

    public Account(String owner, long cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        if (owner == null || owner.trim().length() == 0) {
            throw new IllegalArgumentException("The account must have a owner");
        }
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.balanceCents = new AtomicLong(cents);
    }

    public Account(String owner) {
        this(owner,0);
    }

    public UUID getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Long getBalanceCents() {
        return balanceCents.longValue();
    }


    public void withdraw(long cents) {
        balanceCents.addAndGet(-cents);
    }

    public void deposit(long cents) {
        balanceCents.addAndGet(cents);
    }


}
