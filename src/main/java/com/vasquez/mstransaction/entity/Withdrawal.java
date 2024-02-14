package com.vasquez.mstransaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Withdrawal entity.
 *
 * @author Vasquez
 * @version 1.0.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "withdrawal")
public class Withdrawal {

  @Id
  private String transactionId;

  private String clientId;

  private String productId;

  private String accountNumber;

  private Double transactionAmount;

  private Double commissionPerTransaction;

  private String transactionDate;

}
