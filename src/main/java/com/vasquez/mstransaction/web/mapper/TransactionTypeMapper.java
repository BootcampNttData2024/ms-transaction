package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.TransactionType;
import com.vasquez.mstransaction.model.TransactionTypeModel;
import org.springframework.beans.BeanUtils;

/**
 * Transfer type mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class TransactionTypeMapper {

  TransactionTypeMapper() {
  }

  /**
   * Transfer type request to entity.
   *
   * @param transactionTypeRequest transaction type request.
   * @return transaction type entity.
   */
  public static TransactionType toEntity(TransactionTypeModel transactionTypeRequest) {
    TransactionType transactionType = new TransactionType();
    BeanUtils.copyProperties(transactionTypeRequest, transactionType);
    return transactionType;
  }

  /**
   * Transfer type entity to response.
   *
   * @param transactionType transaction type entity.
   * @return transaction type response.
   */
  public static TransactionTypeModel toResponse(TransactionType transactionType) {
    TransactionTypeModel transactionTypeResponse = new TransactionTypeModel();
    BeanUtils.copyProperties(transactionType, transactionTypeResponse);
    return transactionTypeResponse;
  }

}
