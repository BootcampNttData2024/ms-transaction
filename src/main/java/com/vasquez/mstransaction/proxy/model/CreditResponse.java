package com.vasquez.mstransaction.proxy.model;

import lombok.Data;

/**
 * Credit response.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Data
public class CreditResponse {

  private String creditId;

  private String clientId;

  private String productId;

  private String cardNumber;

  private Double amountRequested;

  private Double amountPaid;

  private Double debtAmount;

  private Integer maxMonthsOfPayment;

  private Double monthlyFee;

  private String requestedDate;

  private String paymentDueDate;

}
