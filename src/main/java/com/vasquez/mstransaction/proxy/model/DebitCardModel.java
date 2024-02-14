package com.vasquez.mstransaction.proxy.model;

import lombok.Data;


import java.util.List;

/**
 * Debit card model
 *
 * @author Vasquez
 * @version 1.0
 */
@Data
public class DebitCardModel {

  private String debitCardId;

  private String productId;

  private String clientId;

  private String cardNumber;

  private Double availableAmount;

  private String expirationDate;

  private String associatedMainAccount;

  private List<String> otherAssociatedAccounts;

}
