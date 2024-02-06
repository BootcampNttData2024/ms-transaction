package com.vasquez.mstransaction.entity;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Transaction type entity.
 *
 * @author Vasquez
 * @version 1.0.
 */
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
