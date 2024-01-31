package com.vasquez.mstransaction.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction")
public class Transaction {

    @Id
    private String transactionId;

    @NotNull
    private String transactionTypeId;

    @NotNull
    private String fromClientId;

    @NotNull
    private String fromProductId;

    @NotNull
    private String fromAccountNumber;

    @NotNull
    private String toClientId;

    @NotNull
    private String toProductId;

    @NotNull
    private String toAccountNumber;

    @NotNull
    private Double transactionAmount;

    private LocalDateTime transactionDate = LocalDateTime.now();

}
