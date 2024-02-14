package com.vasquez.mstransaction.proxy.model;

import lombok.Data;

/**
 * Yanki model
 *
 * @author Vasquez
 * @version 1.0
 */
@Data
public class YankiModel {

  private String yankiId;

  private String documentType;

  private String documentNumber;

  private String phoneNumber;

  private String imeiNumber;

  private String email;

  private Double balance;

  private String associatedDebitCard;

}
