package com.vasquez.mstransaction.service;

import com.vasquez.mstransaction.entity.TransactionType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionTypeService {

    Mono<TransactionType> create(TransactionType request);

    Mono<TransactionType> update(TransactionType request, String transactionTypeId);

    Mono<TransactionType> findById(String transactionTypeId);

    Flux<TransactionType> findAll();

    Mono<Void> deleteById(String transactionTypeId);

}
