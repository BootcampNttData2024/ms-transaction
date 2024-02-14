package com.vasquez.mstransaction.kafka.producer;

import com.vasquez.mstransaction.kafka.event.KafkaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.UUID;

/**
 * Kafka producer
 *
 * @author Vasquez
 * @version 1.0
 */
@Component
public class KafkaProducer<T> {

  private final KafkaTemplate<String, KafkaEvent<T>> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, KafkaEvent<T>> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publish(String topic, String eventType, T data) {
    KafkaEvent<T> kafkaEvent = new KafkaEvent<>();
    kafkaEvent.setId(UUID.randomUUID().toString());
    kafkaEvent.setDate(new Date());
    kafkaEvent.setEventType(eventType);
    kafkaEvent.setData(data);
    kafkaTemplate.send(topic, kafkaEvent);

  }

}
