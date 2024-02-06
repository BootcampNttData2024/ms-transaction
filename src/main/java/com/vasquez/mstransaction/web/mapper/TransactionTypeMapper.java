package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.TransactionType;
import com.vasquez.mstransaction.model.TransactionTypeRequest;
import com.vasquez.mstransaction.model.TransactionTypeResponse;
import org.springframework.beans.BeanUtils;

/**
 * Transaction type mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class TransactionTypeMapper {

  /**
   * Transaction type request to entity.
   *
   * @param transactionTypeRequest transaction type request.
   * @return transaction type entity.
   */
  public static TransactionType toEntity(TransactionTypeRequest transactionTypeRequest) {
    TransactionType transactionType = new TransactionType();
    BeanUtils.copyProperties(transactionTypeRequest, transactionType);
    return transactionType;
  }

  /**
   * Transaction type entity to response.
   *
   * @param transactionType transaction type entity.
   * @return transaction type response.
   */
  public static TransactionTypeResponse toResponse(TransactionType transactionType) {
    TransactionTypeResponse transactionTypeResponse = new TransactionTypeResponse();
    BeanUtils.copyProperties(transactionType, transactionTypeResponse);
    return transactionTypeResponse;
  }

}
