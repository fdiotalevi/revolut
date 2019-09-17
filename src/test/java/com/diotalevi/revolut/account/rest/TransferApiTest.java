package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.service.TransferReceipt;
import kong.unirest.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static com.diotalevi.revolut.account.rest.RestTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferApiTest {


    @BeforeClass
    public static void startupServer() {
        RestTestUtils.initialize();
    }

    @Test
    public void canTransferMoney() {
        UUID fromAccountId = createAccount(new AccountCreationRequest(200, "from")).getBody().getId();
        UUID toAccountId = createAccount(new AccountCreationRequest(0, "to")).getBody().getId();

        transferMoney(new TransferRequest(fromAccountId, toAccountId, 20));

        assertEquals(180, getAccount(fromAccountId).getBody().getBalanceCents().longValue());
        assertEquals(20, getAccount(toAccountId).getBody().getBalanceCents().longValue());

        transferMoney(new TransferRequest(fromAccountId, toAccountId, 180));

        assertEquals(0, getAccount(fromAccountId).getBody().getBalanceCents().longValue());
        assertEquals(200, getAccount(toAccountId).getBody().getBalanceCents().longValue());
    }

    @Test
    public void moneyTransferIssuesAReceipt() {
        UUID fromAccountId = createAccount(new AccountCreationRequest(200, "from")).getBody().getId();
        UUID toAccountId = createAccount(new AccountCreationRequest(0, "to")).getBody().getId();

        HttpResponse<TransferReceipt> receipt = transferMoney(new TransferRequest(fromAccountId, toAccountId, 20));

        assertEquals(fromAccountId, receipt.getBody().getFromAccount());
        assertEquals(toAccountId, receipt.getBody().getToAccount());
        assertEquals(20, receipt.getBody().getAmountCents().longValue());
        assertNotNull(receipt.getBody().getId());
    }

    @Test
    public void canGetATransferReceipt() {
        UUID fromAccountId = createAccount(new AccountCreationRequest(21, "from")).getBody().getId();
        UUID toAccountId = createAccount(new AccountCreationRequest(0, "to")).getBody().getId();

        UUID receiptId = transferMoney(new TransferRequest(fromAccountId, toAccountId, 21)).getBody().getId();
        HttpResponse<TransferReceipt> receipt = getReceipt(receiptId);

        assertEquals(fromAccountId, receipt.getBody().getFromAccount());
        assertEquals(toAccountId, receipt.getBody().getToAccount());
        assertEquals(21, receipt.getBody().getAmountCents().longValue());
        assertEquals(receiptId, receipt.getBody().getId());
    }

    @Test
    public void return404IfReceiptDoesntExist() {
        HttpResponse<TransferReceipt> receipt = getReceipt(UUID.randomUUID());
        assertEquals(HttpStatus.SC_NOT_FOUND, receipt.getStatus());
    }

    @Test
    public void returns400IfGoingOverdraft() {
        UUID fromAccountId = createAccount(new AccountCreationRequest(200, "from")).getBody().getId();
        UUID toAccountId = createAccount(new AccountCreationRequest(0, "to")).getBody().getId();

        HttpResponse<TransferReceipt> response = transferMoney(new TransferRequest(fromAccountId, toAccountId, 201));
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());

    }

}
