package com.vasquez.mstransaction.business.impl;

import com.vasquez.mstransaction.business.DepositService;
import com.vasquez.mstransaction.business.TransferService;
import com.vasquez.mstransaction.business.WithdrawalService;
import com.vasquez.mstransaction.business.exception.AppException;
import com.vasquez.mstransaction.entity.Withdrawal;
import com.vasquez.mstransaction.entity.enums.ProductType;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.ProductProxy;
import com.vasquez.mstransaction.repository.WithdrawalRepository;
import com.vasquez.mstransaction.util.AppUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Withdrawal service implementation.
 *
 * @author Vasquez
 * @version 1.0
 */
@Log4j2
@Service
public class WithdrawalServiceImpl implements WithdrawalService {

  private final WithdrawalRepository withdrawalRepository;
  private final TransferService transferService;
  private final ProductProxy productProxy;
  private final AccountProxy accountProxy;
  private final DepositService depositService;

  public WithdrawalServiceImpl(WithdrawalRepository withdrawalRepository, TransferService transferService, ProductProxy productProxy, AccountProxy accountProxy, @Lazy DepositService depositService) {
    this.withdrawalRepository = withdrawalRepository;
    this.transferService = transferService;
    this.productProxy = productProxy;
    this.accountProxy = accountProxy;
    this.depositService = depositService;
  }

  @Override
  public Mono<Withdrawal> save(Withdrawal request) {
    return accountProxy.getByAccountNumber(request.getAccountNumber())
      .switchIfEmpty(Mono.error(AppException.notFound("The account number %S does not exist.".formatted(request.getAccountNumber()))))
      .flatMap(account -> {
        Mono<Integer> quantityTransfers = transferService.countByFromClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));
        Mono<Integer> quantityDeposits = depositService.countByClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));
        Mono<Integer> quantityWithdrawals = this.countByClientId(request.getClientId()).flatMap(quantity -> Mono.just(Integer.parseInt(quantity.toString())));

        return Mono.zip(quantityTransfers, quantityDeposits, quantityWithdrawals)
          .doOnNext(quantityTransaction -> log.info("quantityTransaction: " + quantityTransaction))
          .switchIfEmpty(Mono.error(AppException.notFound("The transactionTypeId not found.")))
          .flatMap(quantityTransactions -> {
            int quantityTransaction = (quantityTransactions.getT1() + quantityTransactions.getT2() + quantityTransactions.getT3());

            //validate transactions quantity
            double balanceWithCommissionApplied = request.getTransactionAmount();
            if (quantityTransaction > 20) {
              request.setCommissionPerTransaction(5.0);
              balanceWithCommissionApplied = balanceWithCommissionApplied + request.getCommissionPerTransaction();
            }
            account.setAvailableBalance(account.getAvailableBalance() - balanceWithCommissionApplied);

            //validate available balance
            if (account.getAvailableBalance() < 0)
              return Mono.error(AppException.badRequest("The balance from account number %s is insufficient.".formatted(request.getAccountNumber())));

            accountProxy.updateAccount(account)
              .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
              .subscribe(response -> log.info("withdrawal account successfully completed {}", response));
            request.setClientId(account.getClientId());
            request.setProductId(account.getProductId());
            request.setTransactionDate(AppUtil.localDateTimeToString(LocalDateTime.now()));
            request.setCommissionPerTransaction(0.0);
            return withdrawalRepository.save(request);
          });
      });
  }

  @Override
  public Mono<Withdrawal> update(Withdrawal request, String transactionId) {
    return findById(transactionId)
      .flatMap(withdrawal -> {
        withdrawal.setTransactionAmount(request.getTransactionAmount());
        return withdrawalRepository.save(withdrawal);
      });
  }

  @Override
  public Mono<Withdrawal> findById(String transactionId) {
    return withdrawalRepository.findById(transactionId);
  }

  @Override
  public Flux<Withdrawal> findAll() {
    return withdrawalRepository.findAll();
  }

  @Override
  public Mono<Long> countByClientId(String clientId) {
    return withdrawalRepository.countByClientId(clientId);
  }

}
