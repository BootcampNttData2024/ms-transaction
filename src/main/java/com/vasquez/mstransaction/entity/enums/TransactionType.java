package com.vasquez.mstransaction.entity.enums;

import lombok.Getter;

/**
 * Created by vasquez on 10/10/17.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Getter
public enum TransactionType {

  DEPOSIT("DEPOSITO"),
  WITHDRAWAL("RETIRO"),
  PAY("PAGO");
  private String value;

  TransactionType(String value) {
    this.value = value;
  }

}
