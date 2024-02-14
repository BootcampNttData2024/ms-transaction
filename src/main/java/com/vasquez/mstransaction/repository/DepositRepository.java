package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.Deposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Deposit repository
 *
 * @author Vasquez
 * @version 1.0
 */
@Repository
public interface DepositRepository extends ReactiveMongoRepository<Deposit, String> {

  Mono<Long> countByClientId(String clientId);

}
