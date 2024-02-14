package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Transfer;
import com.vasquez.mstransaction.model.TransferModel;
import org.springframework.beans.BeanUtils;

/**
 * Transfer mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class TransferMapper {

  TransferMapper() {
  }

  /**
   * Transfer request to entity.
   *
   * @param transactionRequest transaction request.
   * @return transaction entity.
   */
  public static Transfer toEntity(TransferModel transactionRequest) {
    Transfer transfer = new Transfer();
    BeanUtils.copyProperties(transactionRequest, transfer);
    return transfer;
  }

  /**
   * Transfer entity to response.
   *
   * @param transfer transfer entity.
   * @return transfer response.
   */
  public static TransferModel toResponse(Transfer transfer) {
    TransferModel transactionResponse = new TransferModel();
    BeanUtils.copyProperties(transfer, transactionResponse);
    return transactionResponse;
  }

}
