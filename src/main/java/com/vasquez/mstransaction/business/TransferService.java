package com.vasquez.mstransaction.business;

import com.vasquez.mstransaction.entity.Transfer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transfer service.
 *
 * @author Vasquez
 * @version 1.0.
 */
public interface TransferService {

  Mono<Transfer> create(Transfer request);

  Mono<Transfer> update(Transfer request, String transactionId);

  Mono<Transfer> findById(String transactionId);

  Flux<Transfer> findAll();

  Mono<Void> deleteById(String transactionId);

  Mono<Long> countByFromClientId(String fromClientId);

  Flux<Transfer> findByFromClientId(String fromClientId);

}
