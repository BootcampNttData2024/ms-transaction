package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Withdrawal;
import com.vasquez.mstransaction.model.WithdrawalModel;
import org.springframework.beans.BeanUtils;

/**
 * Withdrawal mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class WithdrawalMapper {

  WithdrawalMapper() {
  }

  /**
   * Withdrawal request to entity.
   *
   * @param transactionRequest transaction request.
   * @return transaction entity.
   */
  public static Withdrawal toEntity(WithdrawalModel transactionRequest) {
    Withdrawal transfer = new Withdrawal();
    BeanUtils.copyProperties(transactionRequest, transfer);
    return transfer;
  }

  /**
   * Withdrawal entity to response.
   *
   * @param transfer transfer entity.
   * @return transfer response.
   */
  public static WithdrawalModel toResponse(Withdrawal transfer) {
    WithdrawalModel transactionResponse = new WithdrawalModel();
    BeanUtils.copyProperties(transfer, transactionResponse);
    return transactionResponse;
  }

}
