package com.vasquez.mstransaction.controller;


import com.vasquez.mstransaction.api.TransactionApi;
import com.vasquez.mstransaction.model.TransactionRequest;
import com.vasquez.mstransaction.model.TransactionResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionRestController implements TransactionApi {

    @Override
    public Mono<TransactionResponse> addTransaction(Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<Void> deleteTransactionById(String transactionId, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<Flux<TransactionResponse>> getAllTransactions(ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<TransactionResponse> getTransactionById(String transactionId, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<TransactionResponse> updateTransaction(String transactionId, Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return null;
    }
}
