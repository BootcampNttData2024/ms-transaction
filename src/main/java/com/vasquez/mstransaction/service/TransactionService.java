package com.vasquez.mstransaction.service;

import com.vasquez.mstransaction.entity.Transaction;
import com.vasquez.mstransaction.repository.TransactionRepository;
import com.vasquez.mstransaction.util.CrudUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService implements CrudUtil<Transaction, String> {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<Transaction> create(Transaction request) {
        return transactionRepository.save(request);
    }

    @Override
    public Mono<Transaction> update(Transaction request, String id) {
        return transactionRepository.save(request);
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return transactionRepository.deleteById(id);
    }
}
