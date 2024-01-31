package com.vasquez.mstransaction.proxy;

import lombok.Data;

@Data
public class AccountResponse {

    private String accountId;

    private String clientId;

    private String productBusinessRuleId;

    private String businessEntity;

    private String accountNumber;

    private Double availableBalance;

    private String headlinesIds;

    private String signatoriesIds;

}
