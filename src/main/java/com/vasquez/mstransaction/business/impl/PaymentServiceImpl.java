package com.vasquez.mstransaction.business.impl;

import com.vasquez.mstransaction.business.PaymentService;
import com.vasquez.mstransaction.business.TransferService;
import com.vasquez.mstransaction.business.WithdrawalService;
import com.vasquez.mstransaction.business.exception.AppException;
import com.vasquez.mstransaction.entity.Payment;
import com.vasquez.mstransaction.entity.enums.ProductType;
import com.vasquez.mstransaction.proxy.AccountProxy;
import com.vasquez.mstransaction.proxy.CreditProxy;
import com.vasquez.mstransaction.proxy.ProductProxy;
import com.vasquez.mstransaction.proxy.YankiProxy;
import com.vasquez.mstransaction.proxy.model.CreditModel;
import com.vasquez.mstransaction.proxy.model.YankiModel;
import com.vasquez.mstransaction.repository.PaymentRepository;
import com.vasquez.mstransaction.util.AppUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Payment service implementation.
 *
 * @author vasquez
 * @version 1.0
 */
@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final ProductProxy productProxy;
  private final AccountProxy accountProxy;
  private final CreditProxy creditProxy;
  private final YankiProxy yankiProxy;

  public PaymentServiceImpl(PaymentRepository paymentRepository, ProductProxy productProxy, AccountProxy accountProxy, CreditProxy creditProxy, YankiProxy yankiProxy) {
    this.paymentRepository = paymentRepository;
    this.creditProxy = creditProxy;
    this.productProxy = productProxy;
    this.accountProxy = accountProxy;
    this.yankiProxy = yankiProxy;
  }

  @Override
  public Mono<Payment> save(Payment request) {
    return productProxy.getProductById(request.getProductId())
      .switchIfEmpty(Mono.error(AppException.notFound("Product with id " + request.getProductId() + " not found")))
      .flatMap(product -> {
        Predicate<String> evaluateProductType = value -> product.getName().equals(value);
        boolean isCreditCard = evaluateProductType.test(ProductType.CREDIT_CARD.getValue());
        boolean isYankMobileWallet = evaluateProductType.test(ProductType.YANKI_MOBILE_WALLET.getValue());

        request.setProductId(product.getProductId());
        request.setTransactionDate(AppUtil.localDateTimeToString(LocalDateTime.now()));

        if (isCreditCard)
          return payCreditProduct(request);
        if (isYankMobileWallet)
          return transactionToYankiMobilleWallet(request);
        return null;
      });
  }

  @Override
  public Mono<Payment> update(Payment request, String transactionId) {
    return findById(transactionId)
      .flatMap(payment -> {
        payment.setTransactionAmount(payment.getTransactionAmount());
        return paymentRepository.save(payment);
      });
  }

  @Override
  public Mono<Payment> findById(String transactionId) {
    return paymentRepository.findById(transactionId);
  }

  @Override
  public Flux<Payment> findAll() {
    return paymentRepository.findAll();
  }

  @Override
  public Mono<Long> countByClientId(String clientId) {
    return paymentRepository.countByClientId(clientId);
  }

  private Mono<Payment> payCreditProduct(Payment request) {
    CreditModel credit = new CreditModel();
    credit.setAmountPaid(request.getTransactionAmount());
    credit.setCardNumber(request.getCardNumber());
    return creditProxy.modifyCredit(credit)
      .switchIfEmpty(Mono.error(AppException
        .notFound("The accountsProxy.payCreditProduct with cardNumber %s does not exist.".formatted(request.getCardNumber()))))
      .flatMap(creditModel -> {
        request.setClientId(creditModel.getClientId());
        return paymentRepository.save(request);
      });
  }

  private Mono<Payment> transactionToYankiMobilleWallet(Payment request) {
    return Mono.zip(yankiProxy.getYankiByPhoneNumber(request.getFromPhoneNumber()),
        yankiProxy.getYankiByPhoneNumber(request.getToPhoneNumber()))
      .flatMap(yankiResponse -> {
        YankiModel fromYanki = yankiResponse.getT1();
        YankiModel toYanki = yankiResponse.getT2();

        //validate balance available from phoneNumber
        if (fromYanki.getBalance() <= 0
          || fromYanki.getBalance() < request.getTransactionAmount())
          return Mono.error(AppException.badRequest("The balance of phoneNumber " + request.getFromPhoneNumber() + " is insufficient."));

        fromYanki.setBalance(fromYanki.getBalance() - request.getTransactionAmount());
        //process transaction if yank NOT HAVE associated account
        if (toYanki.getAssociatedDebitCard().isEmpty()) {
          toYanki.setBalance(toYanki.getBalance() + request.getTransactionAmount());
          Mono.zip(yankiProxy.updateYanki(fromYanki), yankiProxy.updateYanki(toYanki))
            .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
            .subscribe(response -> log.info("Transfer to Yankee Completed Successfully {}", response));
          request.setClientId("-");
          return paymentRepository.save(request);
        }
        //process transaction if yank HAVE associated account
        return creditProxy.getDebitCardById(toYanki.getAssociatedDebitCard())
          .flatMap(debitCard -> accountProxy.getByAccountById(debitCard.getAssociatedMainAccount())
            .flatMap(toAccount -> {
              toAccount.setAvailableBalance(toAccount.getAvailableBalance() + request.getTransactionAmount());
              Mono.zip(accountProxy.updateAccount(toAccount), yankiProxy.updateYanki(fromYanki))
                .onErrorResume(err -> Mono.error(AppException.badRequest(err.getMessage())))
                .doOnNext(response -> log.info("response, {}", response))
                .flatMap(response -> {
                  request.setClientId(response.getT1().getClientId());
                  return Mono.just(response);
                })
                .subscribe(response -> log.info("Transfer to Associated Account Completed Successfully {}", response));
              return paymentRepository.save(request);
            }));
      });
  }

}
