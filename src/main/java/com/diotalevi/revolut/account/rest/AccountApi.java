package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.service.AccountService;
import com.diotalevi.revolut.account.service.EntityNotExistentException;
import com.diotalevi.revolut.account.service.IncorrectInstructionException;
import com.google.gson.Gson;
import spark.Route;

import java.util.UUID;

public class AccountApi {

    private AccountService accountService;
    private Gson gson = new Gson();

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    public Route createAccountRoute() {
        return (req, res) -> {
            try {
                AccountCreationRequest account = gson.fromJson(req.body(), AccountCreationRequest.class);
                if (account == null) {
                    throw new IncorrectInstructionException("Please specify account owner and balance");
                }
                return gson.toJson(accountService.createAccountWithBalance(account.getOwner(), account.getBalanceCents()));
            }
            catch (IncorrectInstructionException ex) {
                res.status(400);
                return gson.toJson(new ErrorResponse(ex.getMessage()));
            }
        };
    }


    public Route getAccountRoute() {
        return (req, res) -> {
            String accountId = req.params("accountId");
            try {
                return gson.toJson(accountService.getAccount(UUID.fromString(accountId)));
            }
            catch (EntityNotExistentException ex) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Account doesn't exist"));
            }
        };
    }

}
