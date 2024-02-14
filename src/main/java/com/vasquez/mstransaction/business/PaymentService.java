package com.vasquez.mstransaction.business;

import com.vasquez.mstransaction.entity.Payment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Payment service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface PaymentService {

  Mono<Payment> save(Payment request);

  Mono<Payment> update(Payment request, String transactionId);

  Mono<Payment> findById(String transactionId);

  Flux<Payment> findAll();

  Mono<Long> countByClientId(String clientId);

}
