package com.vasquez.mstransaction.controller;

import com.vasquez.mstransaction.api.TransactionTypeApi;
import com.vasquez.mstransaction.model.TransactionTypeRequest;
import com.vasquez.mstransaction.model.TransactionTypeResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionTypeRestController implements TransactionTypeApi {

    @Override
    public Mono<TransactionTypeResponse> addTransactionType(Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<Void> deleteTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<Flux<TransactionTypeResponse>> getAllTransactionType(ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<TransactionTypeResponse> getTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<TransactionTypeResponse> updateTransactionType(String transactionTypeId, Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
        return null;
    }
}
