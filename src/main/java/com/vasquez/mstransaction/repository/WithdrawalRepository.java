package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.Withdrawal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Withdrawal repository
 *
 * @author Vasquez
 * @version 1.0
 */
@Repository
public interface WithdrawalRepository extends ReactiveMongoRepository<Withdrawal, String> {

  Mono<Long> countByClientId(String clientId);

}
