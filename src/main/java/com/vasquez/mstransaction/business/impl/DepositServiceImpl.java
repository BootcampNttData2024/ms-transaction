package com.vasquez.mstransaction.business.impl;

import com.vasquez.mstransaction.business.DepositService;
import com.vasquez.mstransaction.business.TransferService;
import com.vasquez.mstransaction.business.WithdrawalService;
import com.vasquez.mstransaction.business.exception.AppException;
import com.vasquez.mstransaction.entity.Deposit;
import com.vasquez.mstransaction.entity.enums.ProductType;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.ProductProxy;
import com.vasquez.mstransaction.repository.DepositRepository;
import com.vasquez.mstransaction.util.AppUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Deposit service implementation.
 *
 * @author vasquez
 * @version 1.0
 */
@Log4j2
@Service
public class DepositServiceImpl implements DepositService {

  private final DepositRepository depositRepository;
  private final TransferService transferService;
  private final WithdrawalService withdrawalService;
  private final ProductProxy productProxy;
  private final AccountProxy accountProxy;

  public DepositServiceImpl(DepositRepository depositRepository, TransferService transferService, WithdrawalService withdrawalService, ProductProxy productProxy, AccountProxy accountProxy) {
    this.depositRepository = depositRepository;
    this.transferService = transferService;
    this.withdrawalService = withdrawalService;
    this.productProxy = productProxy;
    this.accountProxy = accountProxy;
  }

  @Override
  public Mono<Deposit> save(Deposit request) {
    return accountProxy.getByAccountNumber(request.getAccountNumber())
      .switchIfEmpty(Mono.error(AppException.notFound("The account number %S does not exist.".formatted(request.getAccountNumber()))))
      .flatMap(account -> {
        Mono<Integer> quantityTransfers = transferService.countByFromClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));
        Mono<Integer> quantityDeposits = this.countByClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));
        Mono<Integer> quantityWithdrawals = withdrawalService.countByClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));

        return Mono.zip(quantityTransfers, quantityDeposits, quantityWithdrawals)
          .doOnNext(quantityTransaction -> log.info("quantityTransaction: " + quantityTransaction))
          .switchIfEmpty(Mono.error(AppException.notFound("The transactionTypeId not found.")))
          .flatMap(quantityTransactions -> {
            int quantityTransaction = (quantityTransactions.getT1() + quantityTransactions.getT2() + quantityTransactions.getT3());

            //validate transactions quantity
            double balanceWithCommissionApplied = request.getTransactionAmount();
            if (quantityTransaction > 20) {
              request.setCommissionPerTransaction(5.0);
              balanceWithCommissionApplied = balanceWithCommissionApplied - request.getCommissionPerTransaction();
            }
            account.setAvailableBalance(account.getAvailableBalance() + balanceWithCommissionApplied);
            accountProxy.updateAccount(account)
              .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
              .subscribe(response -> log.info("deposit successfully completed {}", response));
            request.setClientId(account.getClientId());
            request.setProductId(account.getProductId());
            request.setTransactionDate(AppUtil.localDateTimeToString(LocalDateTime.now()));
            request.setCommissionPerTransaction(0.0);
            return depositRepository.save(request);
          });
      });
  }

  @Override
  public Mono<Deposit> update(Deposit request, String transactionId) {
    return findById(transactionId)
      .flatMap(deposit -> {
        deposit.setTransactionAmount(deposit.getTransactionAmount());
        return depositRepository.save(deposit);
      });
  }

  @Override
  public Mono<Deposit> findById(String transactionId) {
    return depositRepository.findById(transactionId);
  }

  @Override
  public Flux<Deposit> findAll() {
    return depositRepository.findAll();
  }

  @Override
  public Mono<Long> countByClientId(String clientId) {
    return depositRepository.countByClientId(clientId);
  }
}
