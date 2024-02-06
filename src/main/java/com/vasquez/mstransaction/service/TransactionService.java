package com.vasquez.mstransaction.service;

import com.vasquez.mstransaction.entity.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transaction service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface TransactionService {

  Mono<Transaction> create(Transaction request);

  Mono<Transaction> update(Transaction request, String transactionId);

  Mono<Transaction> findById(String transactionId);

  Flux<Transaction> findAll();

  Mono<Void> deleteById(String transactionId);

  Mono<Long> countByFromClientId(String fromClientId);

  Flux<Transaction> findByFromClientId(String fromClientId);

}
