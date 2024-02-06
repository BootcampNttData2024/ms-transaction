package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transaction repository.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

  Mono<Long> countByFromClientId(String fromClientId);

  Flux<Transaction> findByFromClientId(String fromClientId);

}
