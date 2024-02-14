package com.vasquez.mstransaction.web;


import com.vasquez.mstransaction.api.TransactionApiDelegate;
import com.vasquez.mstransaction.business.DepositService;
import com.vasquez.mstransaction.business.PaymentService;
import com.vasquez.mstransaction.business.WithdrawalService;
import com.vasquez.mstransaction.model.CommissionReport;
import com.vasquez.mstransaction.business.TransferService;
import com.vasquez.mstransaction.model.DepositModel;
import com.vasquez.mstransaction.model.PaymentModel;
import com.vasquez.mstransaction.model.TransferModel;
import com.vasquez.mstransaction.model.WithdrawalModel;
import com.vasquez.mstransaction.web.mapper.DepositMapper;
import com.vasquez.mstransaction.web.mapper.PaymentMapper;
import com.vasquez.mstransaction.web.mapper.TransferMapper;
import com.vasquez.mstransaction.web.mapper.WithdrawalMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Account transaction api delegate implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class TransactionApiDelegateImpl implements TransactionApiDelegate {

  private final TransferService transferService;
  private final DepositService depositService;
  private final WithdrawalService withdrawalsService;
  private final PaymentService paymentService;

  public TransactionApiDelegateImpl(TransferService transferService, DepositService depositService, WithdrawalService withdrawalsService, PaymentService paymentService) {
    this.transferService = transferService;
    this.depositService = depositService;
    this.withdrawalsService = withdrawalsService;
    this.paymentService = paymentService;
  }

  @Override
  public Mono<ResponseEntity<TransferModel>> addTransfer(Mono<TransferModel> transactionRequest, ServerWebExchange exchange) {
    return transactionRequest
      .map(TransferMapper::toEntity)
      .flatMap(transferService::create)
      .map(TransferMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<DepositModel>> addDeposit(Mono<DepositModel> depositModel, ServerWebExchange exchange) {
    return depositModel
      .map(DepositMapper::toEntity)
      .flatMap(depositService::save)
      .map(DepositMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<WithdrawalModel>> addWithdrawal(Mono<WithdrawalModel> withdrawalModel, ServerWebExchange exchange) {
    return withdrawalModel
      .map(WithdrawalMapper::toEntity)
      .flatMap(withdrawalsService::save)
      .map(WithdrawalMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<PaymentModel>> addPayment(Mono<PaymentModel> paymentModel, ServerWebExchange exchange) {
    return paymentModel
      .map(PaymentMapper::toEntity)
      .flatMap(paymentService::save)
      .map(PaymentMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<TransferModel>>> getAllTransfers(ServerWebExchange exchange) {
    return Mono.just(transferService.findAll()
        .flatMap(res -> Mono.just(TransferMapper.toResponse(res))))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<DepositModel>>> getAllDeposits(ServerWebExchange exchange) {
    return Mono.just(depositService.findAll()
        .flatMap(res -> Mono.just(DepositMapper.toResponse(res))))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<WithdrawalModel>>> getAllWithdrawals(ServerWebExchange exchange) {
    return Mono.just(withdrawalsService.findAll()
        .flatMap(res -> Mono.just(WithdrawalMapper.toResponse(res))))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<PaymentModel>>> getAllPayments(ServerWebExchange exchange) {
    return Mono.just(paymentService.findAll()
        .flatMap(res -> Mono.just(PaymentMapper.toResponse(res))))
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransferModel>> getTransferById(String transactionId, ServerWebExchange exchange) {
    return transferService.findById(transactionId)
      .map(TransferMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<DepositModel>> getDepositById(String transactionId, ServerWebExchange exchange) {
    return depositService.findById(transactionId)
      .map(DepositMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<WithdrawalModel>> getWithdrawalById(String transactionId, ServerWebExchange exchange) {
    return withdrawalsService.findById(transactionId)
      .map(WithdrawalMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<PaymentModel>> getPaymentById(String transactionId, ServerWebExchange exchange) {
    return paymentService.findById(transactionId)
      .map(PaymentMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<TransferModel>> updateTransfer(String transactionId, Mono<TransferModel> transactionRequest, ServerWebExchange exchange) {
    return transactionRequest
      .map(TransferMapper::toEntity)
      .flatMap(tra -> transferService.update(tra, transactionId))
      .map(TransferMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<DepositModel>> updateDeposit(String transactionId, Mono<DepositModel> depositModel, ServerWebExchange exchange) {
    return depositModel
      .map(DepositMapper::toEntity)
      .flatMap(tra -> depositService.update(tra, transactionId))
      .map(DepositMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<WithdrawalModel>> updateWithdrawal(String transactionId, Mono<WithdrawalModel> withdrawalModel, ServerWebExchange exchange) {
    return withdrawalModel
      .map(WithdrawalMapper::toEntity)
      .flatMap(tra -> withdrawalsService.update(tra, transactionId))
      .map(WithdrawalMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<PaymentModel>> updatePayment(String transactionId, Mono<PaymentModel> paymentModel, ServerWebExchange exchange) {
    return paymentModel
      .map(PaymentMapper::toEntity)
      .flatMap(tra -> paymentService.update(tra, transactionId))
      .map(PaymentMapper::toResponse)
      .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<CommissionReport>>> getCommissionReport(String clientId, ServerWebExchange exchange) {
    return Mono.just(transferService.findByFromClientId(clientId)
        .map(transaction -> {
          CommissionReport commissionReport = new CommissionReport();
          commissionReport.setTransactionId(transaction.getTransactionId());
          commissionReport.setProductId(transaction.getFromProductId());
          //commissionReport.setCommissionCharged(transaction.getCommissionPerTransaction());
          commissionReport.setTransactionDate(transaction.getTransactionDate());
          return commissionReport;
        }))
      .map(ResponseEntity::ok);
  }

}
