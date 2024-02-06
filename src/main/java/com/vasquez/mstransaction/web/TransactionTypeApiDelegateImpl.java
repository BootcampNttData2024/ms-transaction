package com.vasquez.mstransaction.web;

import com.vasquez.mstransaction.api.TransactionTypeApiDelegate;
import com.vasquez.mstransaction.model.TransactionTypeRequest;
import com.vasquez.mstransaction.model.TransactionTypeResponse;
import com.vasquez.mstransaction.service.TransactionTypeService;
import com.vasquez.mstransaction.web.mapper.TransactionTypeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transaction type api delegate implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class TransactionTypeApiDelegateImpl implements TransactionTypeApiDelegate {

  private final TransactionTypeService transactionTypeService;

  public TransactionTypeApiDelegateImpl(TransactionTypeService transactionTypeService) {
    this.transactionTypeService = transactionTypeService;
  }

  @Override
  public Mono<ResponseEntity<TransactionTypeResponse>> addTransactionType(Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
    return transactionTypeRequest
      .map(TransactionTypeMapper::toEntity)
      .flatMap(transactionTypeService::create)
      .map(TransactionTypeMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
    return transactionTypeService.deleteById(transactionTypeId)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<TransactionTypeResponse>>> getAllTransactionType(ServerWebExchange exchange) {
    return Mono.just(transactionTypeService.findAll()
        .map(TransactionTypeMapper::toResponse))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransactionTypeResponse>> getTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
    return transactionTypeService.findById(transactionTypeId)
      .map(TransactionTypeMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransactionTypeResponse>> updateTransactionType(String transactionTypeId, Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
    return transactionTypeRequest
      .map(TransactionTypeMapper::toEntity)
      .flatMap(traTyp -> transactionTypeService.update(traTyp, transactionTypeId))
      .map(TransactionTypeMapper::toResponse)
      .map(ResponseEntity::ok);
  }
}
