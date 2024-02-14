package com.vasquez.mstransaction.business;

import com.vasquez.mstransaction.entity.TransactionType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transfer type service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface TransactionTypeService {

  Mono<TransactionType> create(TransactionType request);

  Mono<TransactionType> update(TransactionType request, String transactionTypeId);

  Mono<TransactionType> findById(String transactionTypeId);

  Flux<TransactionType> findAll();

  Mono<Void> deleteById(String transactionTypeId);

}
