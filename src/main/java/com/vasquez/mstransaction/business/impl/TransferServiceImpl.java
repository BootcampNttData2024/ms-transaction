package com.vasquez.mstransaction.business.impl;

import com.vasquez.mstransaction.business.TransactionTypeService;
import com.vasquez.mstransaction.entity.Transfer;
import com.vasquez.mstransaction.entity.enums.ProductType;
import com.vasquez.mstransaction.kafka.producer.KafkaProducer;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.CreditProxy;
import com.vasquez.mstransaction.proxy.ProductProxy;
import com.vasquez.mstransaction.proxy.YankiProxy;
import com.vasquez.mstransaction.proxy.model.AccountModel;
import com.vasquez.mstransaction.proxy.model.CreditModel;
import com.vasquez.mstransaction.proxy.model.YankiModel;
import com.vasquez.mstransaction.repository.TransferRepository;
import com.vasquez.mstransaction.business.TransferService;
import com.vasquez.mstransaction.business.exception.AppException;
import com.vasquez.mstransaction.util.AppUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Transfer service implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Log4j2
@Service
public class TransferServiceImpl implements TransferService {

  private final TransferRepository transferRepository;
  private final AccountProxy accountProxy;
  private final KafkaProducer<Transfer> kafkaProducer;

  /**
   * Constructor.
   *
   * @param transferRepository transaction repository.
   * @param accountProxy       account proxy
   */
  public TransferServiceImpl(TransferRepository transferRepository, AccountProxy accountProxy, KafkaProducer<Transfer> kafkaProducer) {
    this.transferRepository = transferRepository;
    this.accountProxy = accountProxy;
    this.kafkaProducer = kafkaProducer;
  }

  @Override
  public Mono<Transfer> create(Transfer request) {
    Mono<AccountModel> fromAccountModelMono = accountProxy.getByAccountNumber(request.getFromAccountNumber());
    Mono<AccountModel> toAccountModelMono = accountProxy.getByAccountNumber(request.getToAccountNumber());
    return Mono.zip(fromAccountModelMono, toAccountModelMono)
      .switchIfEmpty(Mono.error(AppException.notFound("Accounts number %s, %s not found"
        .formatted(request.getFromAccountNumber(), request.getToAccountNumber()))))
      .doOnNext(accountsResponse -> log.info("accountsResponse, {}", accountsResponse))
      .flatMap(accountsResponse -> {
        AccountModel fromAccount = accountsResponse.getT1();
        AccountModel toAccount = accountsResponse.getT2();

        //validate balance available fromAccountNumber
        if (fromAccount.getAvailableBalance() <= 0
          || fromAccount.getAvailableBalance() < request.getTransactionAmount())
          return Mono.error(AppException.badRequest("The balance of fromAccountNumber %s is insufficient.".formatted(request.getFromAccountNumber())));

        //process transactions
        fromAccount.setAvailableBalance(fromAccount.getAvailableBalance() - request.getTransactionAmount());
        toAccount.setAvailableBalance(toAccount.getAvailableBalance() + request.getTransactionAmount());
        Mono.zip(accountProxy.updateAccount(fromAccount), accountProxy.updateAccount(toAccount))
          .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
          .subscribe(response -> log.info("transfer account successfully completed {}", response));
        request.setFromClientId(fromAccount.getClientId());
        request.setFromProductId(fromAccount.getProductId());
        request.setToClientId(toAccount.getClientId());
        request.setToProductId(toAccount.getProductId());
        request.setTransactionDate(AppUtil.localDateTimeToString(LocalDateTime.now()));
        log.info("transfer account request {}", request);
        return transferRepository.save(request);
      });
  }

  @Override
  public Mono<Transfer> update(Transfer request, String id) {
    return this.findById(id).flatMap(transfer -> {
      transfer.setFromAccountNumber(request.getFromAccountNumber());
      transfer.setToAccountNumber(request.getToAccountNumber());
      return transferRepository.save(transfer);
    });
  }

  @Override
  public Mono<Transfer> findById(String id) {
    return transferRepository.findById(id);
  }

  @Override
  public Flux<Transfer> findAll() {
    Transfer transfer = new Transfer();
    transfer.setFromAccountNumber("EJEMPLO DE PRODUCER");
    kafkaProducer.publish("TRANSACTION_YANKI", "UPDATED", transfer);
    return transferRepository.findAll();
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return transferRepository.deleteById(id);
  }

  @Override
  public Mono<Long> countByFromClientId(String fromClientId) {
    return transferRepository.countByFromClientId(fromClientId);
  }

  @Override
  public Flux<Transfer> findByFromClientId(String fromClientId) {
    //return transferRepository.findByFromClientId(fromClientId).filter(t -> t.getCommissionPerTransaction() != null);
    return null;
  }

}
