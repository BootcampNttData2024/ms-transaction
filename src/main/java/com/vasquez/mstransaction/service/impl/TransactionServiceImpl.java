package com.vasquez.mstransaction.service.impl;

import com.vasquez.mstransaction.entity.Transaction;
import com.vasquez.mstransaction.entity.enums.TransactionType;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.AccountResponse;
import com.vasquez.mstransaction.repository.TransactionRepository;
import com.vasquez.mstransaction.service.TransactionService;
import com.vasquez.mstransaction.service.exception.AppException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionTypeServiceImpl transactionTypeService;
    private final AccountProxy accountProxy;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionTypeServiceImpl transactionTypeService, AccountProxy accountProxy) {
        this.transactionRepository = transactionRepository;
        this.transactionTypeService = transactionTypeService;
        this.accountProxy = accountProxy;
    }

    @Override
    public Mono<Transaction> create(Transaction request) {
        return Mono.zip(accountProxy.getByAccountNumber(request.getFromAccountNumber()),
                        accountProxy.getByAccountNumber(request.getToAccountNumber()))
                .switchIfEmpty(Mono.error(AppException.notFound("The accountsProxy.getByAccountNumber not found.")))
                .flatMap(accounts -> {
                    AccountResponse fromAccount = accounts.getT1();
                    AccountResponse toAccount = accounts.getT2();
                    //validate balance available fromAccountNumber
                    if (fromAccount.getAvailableBalance() <= 0
                            || fromAccount.getAvailableBalance() < request.getTransactionAmount())
                        return Mono.error(AppException.badRequest("The balance of fromAccountNumber is insufficient."));

                    return transactionTypeService.findById(request.getTransactionTypeId())
                            .switchIfEmpty(Mono.error(AppException.notFound("The transactionTypeId not found.")))
                            .flatMap(traType -> {
                                if (TransactionType.DEPOSIT.getValue().equals(traType.getName())) {
                                    fromAccount.setAvailableBalance(fromAccount.getAvailableBalance() - request.getTransactionAmount());
                                    toAccount.setAvailableBalance(toAccount.getAvailableBalance() + request.getTransactionAmount());
                                    Mono.zip(accountProxy.updateAccount(fromAccount), accountProxy.updateAccount(toAccount))
                                            .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
                                            .subscribe(response -> System.out.println("deposit successfully..."));
                                }
                                if (TransactionType.WITHDRAWAL.getValue().equals(traType.getName())) {
                                    fromAccount.setAvailableBalance(fromAccount.getAvailableBalance() - request.getTransactionAmount());
                                    accountProxy.updateAccount(fromAccount)
                                            .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
                                            .subscribe(response -> System.out.println("withdrawal successfully..."));
                                    request.setToAccountNumber(null);
                                    request.setToClientId(null);
                                    request.setToProductId(null);
                                }
                                return transactionRepository.save(request);
                            });
                });
    }

    @Override
    public Mono<Transaction> update(Transaction request, String id) {
        return this.findById(id).flatMap(transaction -> {
            transaction.setFromAccountNumber(request.getFromAccountNumber());
            transaction.setToAccountNumber(request.getToAccountNumber());
            return transactionRepository.save(transaction);
        });
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
