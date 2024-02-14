package com.vasquez.mstransaction.business;

import com.vasquez.mstransaction.entity.Withdrawal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Withdrawal service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface WithdrawalService {

  Mono<Withdrawal> save(Withdrawal request);

  Mono<Withdrawal> update(Withdrawal request, String transactionId);

  Mono<Withdrawal> findById(String transactionId);

  Flux<Withdrawal> findAll();

  Mono<Long> countByClientId(String clientId);

}
