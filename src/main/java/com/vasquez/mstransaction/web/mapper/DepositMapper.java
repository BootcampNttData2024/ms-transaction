package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Deposit;
import com.vasquez.mstransaction.model.DepositModel;
import org.springframework.beans.BeanUtils;

/**
 * Deposit mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class DepositMapper {

  DepositMapper() { }

  /**
   * Deposit request to entity.
   *
   * @param transactionRequest transaction request.
   * @return transaction entity.
   */
  public static Deposit toEntity(DepositModel transactionRequest) {
    Deposit transfer = new Deposit();
    BeanUtils.copyProperties(transactionRequest, transfer);
    return transfer;
  }

  /**
   * Deposit entity to response.
   *
   * @param transfer transfer entity.
   * @return transfer response.
   */
  public static DepositModel toResponse(Deposit transfer) {
    DepositModel transactionResponse = new DepositModel();
    BeanUtils.copyProperties(transfer, transactionResponse);
    return transactionResponse;
  }

}
