package com.vasquez.mstransaction.service;

import com.vasquez.mstransaction.entity.TransactionType;
import com.vasquez.mstransaction.repository.TransactionTypeRepository;
import com.vasquez.mstransaction.util.CrudUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionTypeService implements CrudUtil<TransactionType, String> {

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public Mono<TransactionType> create(TransactionType request) {
        return transactionTypeRepository.save(request);
    }

    @Override
    public Mono<TransactionType> update(TransactionType request, String id) {
        return transactionTypeRepository.save(request);
    }

    @Override
    public Mono<TransactionType> findById(String id) {
        return transactionTypeRepository.findById(id);
    }

    @Override
    public Flux<TransactionType> findAll() {
        return transactionTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return transactionTypeRepository.deleteById(id);
    }
}
