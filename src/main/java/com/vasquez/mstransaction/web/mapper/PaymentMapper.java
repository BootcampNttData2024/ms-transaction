package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Payment;
import com.vasquez.mstransaction.model.PaymentModel;
import org.springframework.beans.BeanUtils;

/**
 * Payment mapper.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class PaymentMapper {

  PaymentMapper() {
  }

  /**
   * Payment request to entity.
   *
   * @param transactionRequest transaction request.
   * @return transaction entity.
   */
  public static Payment toEntity(PaymentModel transactionRequest) {
    Payment payment = new Payment();
    BeanUtils.copyProperties(transactionRequest, payment);
    return payment;
  }

  /**
   * Payment entity to response.
   *
   * @param payment payment entity.
   * @return payment response.
   */
  public static PaymentModel toResponse(Payment payment) {
    PaymentModel transactionResponse = new PaymentModel();
    BeanUtils.copyProperties(payment, transactionResponse);
    return transactionResponse;
  }

}
