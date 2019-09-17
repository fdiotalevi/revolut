package com.diotalevi.revolut.account.service;

import java.util.UUID;

public class TransferReceipt {

    private UUID id;
    private UUID fromAccount;
    private UUID toAccount;
    private Long amountCents;

    public TransferReceipt(UUID fromAccount, UUID toAccount, Long amountCents) {
        this.id = UUID.randomUUID();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amountCents = amountCents;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public Long getAmountCents() {
        return amountCents;
    }
}
