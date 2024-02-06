package com.vasquez.mstransaction.proxy.model;

import lombok.Data;

/**
 * Account response.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Data
public class AccountResponse {

  private String accountId;

  private String clientId;

  private String productId;

  private String productBusinessRuleId;

  private String businessEntity;

  private String accountNumber;

  private Double availableBalance;

  private String headlinesIds;

  private String signatoriesIds;

}
