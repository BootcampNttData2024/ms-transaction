package com.vasquez.mstransaction.service.impl;

import com.vasquez.mstransaction.entity.Transaction;
import com.vasquez.mstransaction.entity.enums.TransactionType;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.CreditProxy;
import com.vasquez.mstransaction.proxy.model.AccountResponse;
import com.vasquez.mstransaction.proxy.model.CreditResponse;
import com.vasquez.mstransaction.repository.TransactionRepository;
import com.vasquez.mstransaction.service.TransactionService;
import com.vasquez.mstransaction.service.exception.AppException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.function.Predicate;

/**
 * Transaction service implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Log4j2
@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionTypeServiceImpl transactionTypeService;
  private final AccountProxy accountProxy;

  private final CreditProxy creditProxy;

  /**
   * Constructor.
   *
   * @param transactionRepository  transaction repository.
   * @param transactionTypeService transaction type service.
   * @param accountProxy           account proxy
   * @param creditProxy            credit proxy
   */
  public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionTypeServiceImpl transactionTypeService, AccountProxy accountProxy, CreditProxy creditProxy) {
    this.transactionRepository = transactionRepository;
    this.transactionTypeService = transactionTypeService;
    this.accountProxy = accountProxy;
    this.creditProxy = creditProxy;
  }

  @Override
  public Mono<Transaction> create(Transaction request) {
    return Mono.zip(accountProxy.getByAccountNumber(request.getFromAccountNumber()),
        accountProxy.getByAccountNumber(request.getToAccountNumber()))
      .doOnNext(accounts -> log.info("accountsTuple2: " + accounts.getT1()))
      //.switchIfEmpty(Mono.error(AppException.notFound("The accountsProxy.getByAccountNumber not found.")))
      .flatMap(accounts -> {
        AccountResponse fromAccount = accounts.getT1();
        AccountResponse toAccount = accounts.getT2();

        //validate balance available fromAccountNumber
        if (fromAccount.getAvailableBalance() <= 0
          || fromAccount.getAvailableBalance() < request.getTransactionAmount())
          return Mono.error(AppException.badRequest("The balance of fromAccountNumber " + request.getFromAccountNumber() + " is insufficient."));

        Mono<com.vasquez.mstransaction.entity.TransactionType> transactionTypeMono = transactionTypeService.findById(request.getTransactionTypeId());
        Mono<Integer> quantityTransaction = this.countByFromClientId(fromAccount.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));

        return Mono.zip(transactionTypeMono, quantityTransaction)
          .doOnNext(responseZip2 -> log.info("traType: " + responseZip2.getT1().getName()))
          .switchIfEmpty(Mono.error(AppException.notFound("The transactionTypeId not found.")))
          .flatMap(responseZip2 -> {
            //apply commission per transaction
            double balanceWithCommissionApplied = fromAccount.getAvailableBalance();
            if (responseZip2.getT2() >= 20) {
              request.setCommissionPerTransaction(5.0);
              balanceWithCommissionApplied = balanceWithCommissionApplied - request.getCommissionPerTransaction();
            }

            //define transaction type
            Predicate<String> evaluateTransactionType = value -> value.equals(responseZip2.getT1().getName());
            boolean isDeposit = evaluateTransactionType.test(TransactionType.DEPOSIT.getValue());
            boolean isWithdrawal = evaluateTransactionType.test(TransactionType.WITHDRAWAL.getValue());

            //transaction type DEPOSIT
            if (isDeposit) {
              fromAccount.setAvailableBalance(balanceWithCommissionApplied - request.getTransactionAmount());
              toAccount.setAvailableBalance(toAccount.getAvailableBalance() + request.getTransactionAmount());
              log.info("request from account: {}", fromAccount);
              Mono.zip(accountProxy.updateAccount(fromAccount), accountProxy.updateAccount(toAccount))
                .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
                .subscribe(response -> log.info("deposit successfully completed {}", response));
            }
            //transaction type WITHDRAWAL
            if (isWithdrawal) {
              fromAccount.setAvailableBalance(balanceWithCommissionApplied - request.getTransactionAmount());
              accountProxy.updateAccount(fromAccount)
                .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
                .subscribe(response -> log.info("withdrawal successfully completed {}", response));
              request.setToAccountNumber(null);
              request.setToClientId(null);
              request.setToProductId(null);
            }
            log.info("end service: {}", request);
            return transactionRepository.save(request);
          });
      }).switchIfEmpty(payCreditProduct(request));
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

  @Override
  public Mono<Long> countByFromClientId(String fromClientId) {
    return transactionRepository.countByFromClientId(fromClientId);
  }

  @Override
  public Flux<Transaction> findByFromClientId(String fromClientId) {
    return transactionRepository.findByFromClientId(fromClientId).filter(t -> t.getCommissionPerTransaction() != null);
  }

  private Mono<Transaction> payCreditProduct(Transaction request) {
    return transactionTypeService.findById(request.getTransactionTypeId())
      .flatMap(transactionType -> {
        boolean isTransactionPay = TransactionType.PAY.getValue().equals(transactionType.getName());

        if (isTransactionPay) {
          CreditResponse credit = new CreditResponse();
          credit.setAmountPaid(request.getTransactionAmount());
          credit.setCardNumber(request.getToAccountNumber());
          return creditProxy.modifyCredit(credit)
            .switchIfEmpty(Mono.error(AppException.notFound("The accountsProxy.payCreditProduct not found.")))
            .flatMap(creditResponse -> transactionRepository.save(request));
        }
        return Mono.error(AppException.notFound("The accountsProxy.payCreditProduct not is transaction type pay."));
      });
  }

}
