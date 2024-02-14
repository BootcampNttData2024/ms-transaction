package com.vasquez.mstransaction.business;

import com.vasquez.mstransaction.entity.Deposit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Deposit service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface DepositService {

  Mono<Deposit> save(Deposit request);

  Mono<Deposit> update(Deposit request, String transactionId);

  Mono<Deposit> findById(String transactionId);

  Flux<Deposit> findAll();

  Mono<Long> countByClientId(String clientId);

}
