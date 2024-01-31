package com.vasquez.mstransaction.service;

import com.vasquez.mstransaction.entity.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<Transaction> create(Transaction request);

    Mono<Transaction> update(Transaction request, String transactionId);

    Mono<Transaction> findById(String transactionId);

    Flux<Transaction> findAll();

    Mono<Void> deleteById(String transactionId);

}
