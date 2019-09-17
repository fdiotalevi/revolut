package com.diotalevi.revolut.account;

import com.diotalevi.revolut.account.rest.AccountApi;
import com.diotalevi.revolut.account.rest.ErrorResponse;
import com.diotalevi.revolut.account.rest.TransferApi;
import com.diotalevi.revolut.account.service.AccountService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static spark.Spark.*;

public class StartApiServer {

    public static void main(String[] args) {
        Gson gson = new Gson();
        AccountService accountService = new AccountService();
        AccountApi accountApi = new AccountApi(accountService);
        TransferApi transferApi = new TransferApi(accountService);

        post("/accounts", accountApi.createAccountRoute());
        get("/accounts/:accountId", accountApi.getAccountRoute());

        post("/transfers", transferApi.createTransferRoute());
        get("/transfers/:receiptId", transferApi.getReceiptRoute());

        exception(JsonSyntaxException.class, (exc, req, resp) -> {
            resp.status(400);
            resp.body(gson.toJson(new ErrorResponse("The Json request seems to be invalid. Please check the request body")));
        });
    }



}
