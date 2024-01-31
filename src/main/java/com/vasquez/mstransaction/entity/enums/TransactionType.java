package com.vasquez.mstransaction.entity.enums;

import lombok.Getter;

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
