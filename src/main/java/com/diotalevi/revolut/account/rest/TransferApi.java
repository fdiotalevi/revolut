package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.service.AccountService;
import com.diotalevi.revolut.account.service.EntityNotExistentException;
import com.diotalevi.revolut.account.service.IncorrectInstructionException;
import com.diotalevi.revolut.account.service.TransferReceipt;
import com.google.gson.Gson;
import spark.Route;

import java.util.UUID;

public class TransferApi {

    private AccountService accountService;
    private Gson gson = new Gson();

    public TransferApi(AccountService accountService) {
        this.accountService = accountService;
    }

    public Route createTransferRoute() {
        return (req, res) -> {
            try {
                TransferRequest transferRequest = gson.fromJson(req.body(), TransferRequest.class);
                if (transferRequest == null) {
                    throw new IncorrectInstructionException("Please specify source, destination accounts and amount to transfer");

                }
                TransferReceipt receipt = accountService.transfer(transferRequest.getFromAccount(),
                        transferRequest.getToAccount(),
                        transferRequest.getAmountCents());
                res.status(200);
                return gson.toJson(receipt);
            }
            catch (IncorrectInstructionException ex) {
                res.status(400);
                return gson.toJson(new ErrorResponse(ex.getMessage()));
            }

        };
    }

    public Route getReceiptRoute() {
        return (req, res) -> {
            String receiptId = req.params("receiptId");
            try {
                return gson.toJson(accountService.getTransferReceipt(UUID.fromString(receiptId)));
            }
            catch (EntityNotExistentException ex) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Receipt doesn't exist"));
            }
        };
    }
}
