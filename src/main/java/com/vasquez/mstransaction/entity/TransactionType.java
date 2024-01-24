package com.vasquez.mstransaction.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction_type")
public class TransactionType {

    @Id
    private String transactionTypeId;

    @NotNull
    private String name;

}
