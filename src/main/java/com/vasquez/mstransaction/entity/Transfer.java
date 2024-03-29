package com.vasquez.mstransaction.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Transfer entity.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transfer")
public class Transfer {

  @Id
  private String transactionId;

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

  private String transactionDate;

}
