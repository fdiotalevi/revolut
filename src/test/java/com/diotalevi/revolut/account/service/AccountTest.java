package com.diotalevi.revolut.account.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountTest {

    @Test
    public void newlyCreateAccountHasIdAndZeroBalance() {
        Account account = new Account("Mark");
        assertNotNull(account.getId());
        assertEquals(0l, account.getBalanceCents().longValue());
        assertEquals("Mark", account.getOwner());
    }

    @Test
    public void canCreateAnAccountWithSpecifiedBalance() {
        Account account = new Account("Jess", 120);
        assertNotNull(account.getId());
        assertEquals(120l, account.getBalanceCents().longValue());
        assertEquals("Jess", account.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotInitializeNegativeAccount() {
        Account account = new Account("Ryan", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotInitializeAccountWithoutOwner() {
        Account account = new Account(null, 1);
    }

    @Test
    public void canDeposit() {
        Account account = new Account("Clay");
        account.deposit(101);
        assertEquals(101l, account.getBalanceCents().longValue());
        account.deposit(20);
        assertEquals(121l, account.getBalanceCents().longValue());
    }

    @Test
    public void canWithdraw() {
        Account account = new Account("Bryce", 42);
        account.withdraw(10);
        assertEquals(32l, account.getBalanceCents().longValue());
        account.withdraw(32);
        assertEquals(0l, account.getBalanceCents().longValue());
    }


}
