# Overview

This project provides a simple implementation for the following API
- account creation
- account retrieval 
- money transfer between accounts
- retrieve money transfer receipts

To launch the API server, locate the class 
`com.diotalevi.revolut.account.StartApiServer` and launch it.

Alternatively, you can use Maven typing 
`mvn exec:java`

In both cases, the API server will be available at 
`http://localhost:4567`

# API

The server offers 4 API endpoints
- account creation
- account retrieval 
- money transfer between account
- retrieve money transfer receipt

The file `docs/api-spec.yaml` contains the Swagger spec of the API.

## Account creation

To create an account execute an 
HTTP POST to 
`http://localhost:4567/accounts` with a body like the following

```$json
{
   "owner": "Filippo",
   "balanceCents": 202
}
```

Where 
- *owner* is the name of the account owner
- *balanceCents* is the account balance in cents (optional, if missing 0 will be assumed)


The *owner* field is mandatory. Failure to specify it will cause the API to return HTTP status 400.

If successful, the API will return an HTTP 200 with a body like the following
```$json
{"id":"62dae1d4-ce4d-46af-b2d5-885417408095","owner":"Filippo","balanceCents":202}
``` 

## Account retrieval

Once an account has been created, it can retrieved executing an HHTP GET to the following URL

`http://localhost:4567/accounts/<account-id>`

For instance, to retrieve the information of the account created above, the URL will be

`http://localhost:4567/accounts/62dae1d4-ce4d-46af-b2d5-885417408095`

If successful, the API will return an HTTP 200 with a body like the following
```$json
{"id":"62dae1d4-ce4d-46af-b2d5-885417408095","owner":"Filippo","balanceCents":202}
``` 

## Transfer request

To execute a money transfer execute an HTTP POST to

`http://localhost:4567/transfers`

with a body like the example

```
{
   	"fromAccount": "0c1356fb-03b2-497f-94ab-0662730463c3",
   	"toAccount": "73563eb0-b47f-42e7-9bd1-012b49ea744a",
   	"amountCents": 3
}
```

All the fields are mandatory.

A successful transfer will return a receipt 

```$json
{"id":"fd36b6a7-c8c4-48f0-a8ef-719be01edad9","fromAccount":"0c1356fb-03b2-497f-94ab-0662730463c3","toAccount":"73563eb0-b47f-42e7-9bd1-012b49ea744a","amountCents":3}
```

## Retrieve money transfer receipt

The same transfer receipt can be retrieved later with a HTTP GET at

`http://localhost:4567/transfers/<receipt id>`

# Tests

Execute `mvn test` to launch the tests. These test both the Java code and the API endpoints.

