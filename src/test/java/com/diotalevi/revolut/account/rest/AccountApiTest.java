package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.service.Account;
import kong.unirest.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static com.diotalevi.revolut.account.rest.RestTestUtils.*;
import static org.junit.Assert.assertEquals;

public class AccountApiTest {

    @BeforeClass
    public static void startupServer() {
        initialize();
    }

    @Test
    public void returns400IfNoOnwerSpecified() {
        HttpResponse<Account> response = createAccount(null);
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    public void returns400IfNegativeBalanceSpecified() {
        AccountCreationRequest request = new AccountCreationRequest(-1, "joe");
        HttpResponse<Account> response = createAccount(request);
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    public void canCreateAccount() {
        AccountCreationRequest request = new AccountCreationRequest(100, "joe");
        HttpResponse<Account> response = createAccount(request);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    public void canCreateAccountAndRetrieveIt() {
        AccountCreationRequest request = new AccountCreationRequest(100, "joe");
        UUID newAccountId = createAccount(request).getBody().getId();

        HttpResponse<Account> account = getAccount(newAccountId);

        assertEquals(HttpStatus.SC_OK, account.getStatus());
        assertEquals("joe", account.getBody().getOwner());
        assertEquals(100, account.getBody().getBalanceCents().longValue());
        assertEquals(newAccountId, account.getBody().getId());
    }

    @Test
    public void returns404IfAccountDoesntExist() {
        HttpResponse<Account> account = getAccount(UUID.randomUUID());
        assertEquals(HttpStatus.SC_NOT_FOUND, account.getStatus());
    }

}
