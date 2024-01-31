package com.vasquez.mstransaction.web;

import com.vasquez.mstransaction.api.TransactionTypeApiDelegate;
import com.vasquez.mstransaction.model.TransactionTypeRequest;
import com.vasquez.mstransaction.model.TransactionTypeResponse;
import com.vasquez.mstransaction.service.TransactionTypeService;
import com.vasquez.mstransaction.web.mapper.TransactionTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionTypeApiDelegateImpl implements TransactionTypeApiDelegate {

    private final TransactionTypeService transactionTypeService;

    public TransactionTypeApiDelegateImpl(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @Override
    public Mono<TransactionTypeResponse> addTransactionType(Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
        return transactionTypeRequest.map(TransactionTypeMapper::toEntity)
                .flatMap(transactionTypeService::create)
                .map(TransactionTypeMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
        return transactionTypeService.deleteById(transactionTypeId);
    }

    @Override
    public Mono<Flux<TransactionTypeResponse>> getAllTransactionType(ServerWebExchange exchange) {
        return Mono.just(transactionTypeService.findAll().map(TransactionTypeMapper::toResponse));
    }

    @Override
    public Mono<TransactionTypeResponse> getTransactionTypeById(String transactionTypeId, ServerWebExchange exchange) {
        return transactionTypeService.findById(transactionTypeId).map(TransactionTypeMapper::toResponse);
    }

    @Override
    public Mono<TransactionTypeResponse> updateTransactionType(String transactionTypeId, Mono<TransactionTypeRequest> transactionTypeRequest, ServerWebExchange exchange) {
        return transactionTypeRequest.map(TransactionTypeMapper::toEntity)
                .flatMap(traTyp -> transactionTypeService.update(traTyp, transactionTypeId))
                .map(TransactionTypeMapper::toResponse);
    }
}
