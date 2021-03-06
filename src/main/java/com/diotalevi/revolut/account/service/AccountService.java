package com.diotalevi.revolut.account.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implement the basic account logic
 */
public class AccountService {

    private ConcurrentHashMap<UUID, Account> accountStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, TransferReceipt> receipts = new ConcurrentHashMap<>();

    /**
     * Create a new account with balance 0
     */
    public Account createAccount(String owner) throws IncorrectInstructionException {
        return createAccountWithBalance(owner, 0l);
    }

    /**
     * Create a new account with the specified balance
     */
    public Account createAccountWithBalance(String owner, long cents) throws IncorrectInstructionException {
        if (owner == null) {
            throw new IncorrectInstructionException("The owner of the account must be specified");
        }
        if (cents < 0) {
            throw new IncorrectInstructionException("The account balance cannot be negative");
        }

        Account account = new Account(owner, cents);
        accountStore.put(account.getId(), account);
        return account;
    }

    /**
     * Transfer the specified amount between two accounts
     *
     */
    public TransferReceipt transfer(UUID fromAccountId, UUID toAccountId, long cents) throws IncorrectInstructionException {
        if (cents <= 0) {
            throw new IncorrectInstructionException("Cannot transfer zero or a negative amount");
        }
        if (fromAccountId == null) {
            throw new IncorrectInstructionException("The source account must be specified");
        }
        if (toAccountId == null) {
            throw new IncorrectInstructionException("The destination account must be specified");
        }
        Account fromAccount = accountStore.get(fromAccountId);
        Account toAccount = accountStore.get(toAccountId);
        if (fromAccount == null) {
            throw new IncorrectInstructionException("The source account does not exist");
        }
        if (toAccount == null) {
            throw new IncorrectInstructionException("The destination account does not exist");
        }

        synchronized(fromAccount) { //lock only the debited account
            if (fromAccount.getBalanceCents().longValue() < cents) {
                throw new IncorrectInstructionException("Cannot transfer more than the account balance");
            }
            fromAccount.withdraw(cents);
            toAccount.deposit(cents);
            TransferReceipt receipt = new TransferReceipt(fromAccountId, toAccountId, cents);
            receipts.put(receipt.getId(), receipt);
            return receipt;
        }
    }

    /**
     * Retrieve a transfer receipt
     *
     */
    public TransferReceipt getTransferReceipt(UUID receiptId) throws EntityNotExistentException {
        TransferReceipt receipt = receipts.get(receiptId);
        if (receipt == null) {
            throw new EntityNotExistentException(receiptId);
        }
        return receipt;
    }


    /**
     * Retrieve account details
     *
     */
    public Account getAccount(UUID accountId) throws EntityNotExistentException {
        Account account = accountStore.get(accountId);
        if (account == null) {
            throw new EntityNotExistentException(accountId);
        }
        return account;
    }
}
