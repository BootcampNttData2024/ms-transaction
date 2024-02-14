package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Payment repository
 *
 * @author Vasquez
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {

  Mono<Long> countByClientId(String clientId);

}
