package com.diotalevi.revolut.account.service;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class AccountServiceTest {

    private AccountService accountService = new AccountService();

    @Test
    public void canCreateAndGetEmptyAccount() throws EntityNotExistentException, OperationNotPermittedException {
        UUID accountId = accountService.createAccount("John").getId();

        Account account = accountService.getAccount(accountId);
        assertEquals(0l, account.getBalanceCents().longValue());
        assertEquals("John", account.getOwner());
    }

    @Test
    public void canCreateAndGetAccountWithBalance() throws EntityNotExistentException, OperationNotPermittedException {
        UUID accountId = accountService.createAccountWithBalance("Mark", 101l).getId();
        Account account = accountService.getAccount(accountId);
        assertEquals(101l, account.getBalanceCents().longValue());
        assertEquals("Mark", account.getOwner());
    }

    @Test(expected = EntityNotExistentException.class)
    public void throwExceptionIfAccountDoesntExist() throws EntityNotExistentException {
        accountService.getAccount(UUID.randomUUID());
    }

    @Test
    public void canTransferMoney() throws EntityNotExistentException, OperationNotPermittedException {
        UUID accountFrom = accountService.createAccountWithBalance("Susan", 100).getId();
        UUID accountTo = accountService.createAccount("Tony").getId();
        TransferReceipt transferReceipt = accountService.transfer(accountFrom, accountTo, 42);

        assertEquals(accountFrom, transferReceipt.getFromAccount());
        assertEquals(accountTo, transferReceipt.getToAccount());
        assertEquals(42, transferReceipt.getAmountCents().longValue());
        assertEquals(58l, accountService.getAccount(accountFrom).getBalanceCents().longValue());
        assertEquals(42l, accountService.getAccount(accountTo).getBalanceCents().longValue());
    }

    @Test(expected = OperationNotPermittedException.class)
    public void cannotTransferNegativeAmount() throws OperationNotPermittedException {
        UUID accountFrom = accountService.createAccountWithBalance("Ann", 100).getId();
        UUID accountTo = accountService.createAccount("Joe").getId();
        accountService.transfer(accountFrom, accountTo, -1);
    }

    @Test(expected = OperationNotPermittedException.class)
    public void cannotTransferOverBalance() throws OperationNotPermittedException {
        UUID accountFrom = accountService.createAccountWithBalance("Helen", 100).getId();
        UUID accountTo = accountService.createAccount("Paul").getId();
        accountService.transfer(accountFrom, accountTo, 101);
    }



}
