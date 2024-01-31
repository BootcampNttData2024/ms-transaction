package com.vasquez.mstransaction.web;


import com.vasquez.mstransaction.api.TransactionApiDelegate;
import com.vasquez.mstransaction.model.TransactionRequest;
import com.vasquez.mstransaction.model.TransactionResponse;
import com.vasquez.mstransaction.service.TransactionService;
import com.vasquez.mstransaction.service.impl.TransactionTypeServiceImpl;
import com.vasquez.mstransaction.web.mapper.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionApiDelegateImpl implements TransactionApiDelegate {

    private final TransactionService transactionService;

    public TransactionApiDelegateImpl(TransactionService transactionService, TransactionTypeServiceImpl transactionTypeService) {
        this.transactionService = transactionService;
    }

    @Override
    public Mono<TransactionResponse> addTransaction(Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionRequest.map(TransactionMapper::toEntity)
                .flatMap(transactionService::create)
                .map(TransactionMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteTransactionById(String transactionId, ServerWebExchange exchange) {
        return transactionService.deleteById(transactionId);
    }

    @Override
    public Mono<Flux<TransactionResponse>> getAllTransactions(ServerWebExchange exchange) {
        return Mono.just(transactionService.findAll()
                .flatMap(res -> Mono.just(TransactionMapper.toResponse(res))));
                //.flatMapMany(flux -> flux).collectList().flatMap(r -> Mono.just(Flux.fromIterable(r)));
    }

    @Override
    public Mono<TransactionResponse> getTransactionById(String transactionId, ServerWebExchange exchange) {
        return transactionService.findById(transactionId).map(TransactionMapper::toResponse);
    }

    @Override
    public Mono<TransactionResponse> updateTransaction(String transactionId, Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionRequest.map(TransactionMapper::toEntity)
                .flatMap(tra -> transactionService.update(tra, transactionId))
                .map(TransactionMapper::toResponse);
    }
}
