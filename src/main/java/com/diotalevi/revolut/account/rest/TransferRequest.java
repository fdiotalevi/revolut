package com.diotalevi.revolut.account.rest;

import java.util.UUID;

public class TransferRequest {

    private UUID fromAccount;
    private UUID toAccount;
    private long amountCents;

    public TransferRequest(UUID fromAccount, UUID toAccount, long amountCents) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amountCents = amountCents;
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public long getAmountCents() {
        return amountCents;
    }
}
