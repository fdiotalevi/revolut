package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.service.AccountService;
import com.diotalevi.revolut.account.service.EntityNotExistentException;
import com.google.gson.Gson;
import spark.Route;
import spark.utils.StringUtils;

import java.util.UUID;

public class AccountApi {

    private AccountService accountService;
    private Gson gson = new Gson();

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    public Route createAccountRoute() {
        return (req, res) -> {
            AccountCreationRequest account = gson.fromJson(req.body(), AccountCreationRequest.class);

            if (account == null || StringUtils.isEmpty(account.getOwner())) {
                res.status(400);
                return gson.toJson(new ErrorResponse("The owner of the account must be specified"));
            }

            if (account.getBalanceCents() < 0) {
                res.status(400);
                return gson.toJson(new ErrorResponse("The account balance cannot be negative"));
            }

            return gson.toJson(accountService.createAccountWithBalance(account.getOwner(), account.getBalanceCents()));
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
