package com.vasquez.mstransaction.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudUtil<T, ID> {

    Mono<T> create(T request);

    Mono<T> update(T request, ID id);

    Mono<T> findById(ID id);

    Flux<T> findAll();

    Mono<Void> deleteById(ID id);

}
