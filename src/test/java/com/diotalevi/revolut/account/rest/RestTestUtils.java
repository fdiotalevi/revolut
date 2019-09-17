package com.diotalevi.revolut.account.rest;

import com.diotalevi.revolut.account.StartApiServer;
import com.diotalevi.revolut.account.service.Account;
import com.diotalevi.revolut.account.service.TransferReceipt;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

import java.util.UUID;

public class RestTestUtils {

    public static void initialize() {
        StartApiServer.main(null);
        Unirest.config().setObjectMapper(new ObjectMapper() {

            private Gson gson = new Gson();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return gson.fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });
    }

    public static HttpResponse<Account> createAccount(Object body) {
        return Unirest.post("http://localhost:4567/accounts").body(body).asObject(Account.class);
    }

    public static HttpResponse<Account> getAccount(UUID id) {
        return Unirest.get("http://localhost:4567/accounts/" + id.toString())
                .asObject(Account.class);
    }

    public static HttpResponse<TransferReceipt> transferMoney(TransferRequest request) {
        return Unirest.post("http://localhost:4567/transfers").body(request).asObject(TransferReceipt.class);
    }

    public static HttpResponse<TransferReceipt> getReceipt(UUID receiptId) {
        return Unirest.get("http://localhost:4567/transfers/" + receiptId.toString()).asObject(TransferReceipt.class);
    }

}
