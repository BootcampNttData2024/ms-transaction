package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.Transfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transfer repository.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Repository
public interface TransferRepository extends ReactiveMongoRepository<Transfer, String> {

  Mono<Long> countByFromClientId(String fromClientId);

  Flux<Transfer> findByFromClientId(String fromClientId);

}
