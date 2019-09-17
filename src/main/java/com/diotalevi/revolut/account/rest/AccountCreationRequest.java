package com.diotalevi.revolut.account.rest;

public class AccountCreationRequest {

    private long balanceCents;
    private String owner;

    public AccountCreationRequest(long balanceCents, String owner) {
        this.balanceCents = balanceCents;
        this.owner = owner;
    }

    public long getBalanceCents() {
        return balanceCents;
    }

    public String getOwner() {
        return owner;
    }
}
