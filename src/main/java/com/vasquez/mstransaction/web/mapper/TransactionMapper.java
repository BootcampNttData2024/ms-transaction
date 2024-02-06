package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Transaction;
import com.vasquez.mstransaction.model.TransactionRequest;
import com.vasquez.mstransaction.model.TransactionResponse;
import org.springframework.beans.BeanUtils;

/**
 * Transaction mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class TransactionMapper {

  /**
   * Transaction request to entity.
   *
   * @param transactionRequest transaction request.
   * @return transaction entity.
   */
  public static Transaction toEntity(TransactionRequest transactionRequest) {
    Transaction transaction = new Transaction();
    BeanUtils.copyProperties(transactionRequest, transaction);
    return transaction;
  }

  /**
   * Transaction entity to response.
   *
   * @param transaction transaction entity.
   * @return transaction response.
   */
  public static TransactionResponse toResponse(Transaction transaction) {
    TransactionResponse transactionResponse = new TransactionResponse();
    BeanUtils.copyProperties(transaction, transactionResponse);
    return transactionResponse;
  }

}
