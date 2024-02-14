package com.vasquez.mstransaction.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * Kafka event
 *
 * @author Vasquez
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEvent<T> {

  private String id;
  private Date date;
  private String eventType;
  private T data;

}
