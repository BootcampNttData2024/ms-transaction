package com.vasquez.mstransaction.web;


import com.vasquez.mstransaction.api.TransactionApiDelegate;
import com.vasquez.mstransaction.model.CommissionReport;
import com.vasquez.mstransaction.model.TransactionRequest;
import com.vasquez.mstransaction.model.TransactionResponse;
import com.vasquez.mstransaction.service.TransactionService;
import com.vasquez.mstransaction.service.impl.TransactionTypeServiceImpl;
import com.vasquez.mstransaction.web.mapper.TransactionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transaction api delegate implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class TransactionApiDelegateImpl implements TransactionApiDelegate {

  private final TransactionService transactionService;

  public TransactionApiDelegateImpl(TransactionService transactionService, TransactionTypeServiceImpl transactionTypeService) {
    this.transactionService = transactionService;
  }

  @Override
  public Mono<ResponseEntity<TransactionResponse>> addTransaction(Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
    return transactionRequest
      .map(TransactionMapper::toEntity)
      .flatMap(transactionService::create)
      .map(TransactionMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteTransactionById(String transactionId, ServerWebExchange exchange) {
    return transactionService.deleteById(transactionId)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<TransactionResponse>>> getAllTransactions(ServerWebExchange exchange) {
    return Mono.just(transactionService.findAll()
        .flatMap(res -> Mono.just(TransactionMapper.toResponse(res))))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransactionResponse>> getTransactionById(String transactionId, ServerWebExchange exchange) {
    return transactionService.findById(transactionId)
      .map(TransactionMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransactionResponse>> updateTransaction(String transactionId, Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
    return transactionRequest
      .map(TransactionMapper::toEntity)
      .flatMap(tra -> transactionService.update(tra, transactionId))
      .map(TransactionMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<CommissionReport>>> getCommissionReport(String clientId, ServerWebExchange exchange) {
    return Mono.just(transactionService.findByFromClientId(clientId)
      .map(transaction -> {
        CommissionReport commissionReport = new CommissionReport();
        commissionReport.setTransactionId(transaction.getTransactionId());
        commissionReport.setProductId(transaction.getFromProductId());
        commissionReport.setCommissionCharged(transaction.getCommissionPerTransaction());
        commissionReport.setTransactionDate(transaction.getTransactionDate().toString());
        return commissionReport;
      }))
      .map(ResponseEntity::ok);
  }

}
