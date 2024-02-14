package com.vasquez.mstransaction.proxy;

import com.vasquez.mstransaction.proxy.model.YankiModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Yanki proxy.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class YankiProxy {

  @Value("${microservices.ms-yanki.base-url}")
  private String yankiBaseUrl;

  /**
   * Count yanki by client id.
   *
   * @param id client id
   * @return Mono Integer
   */
  public Mono<YankiModel> getYankiByPhoneNumber(String id) {
    return WebClient.create()
      .get()
      .uri(yankiBaseUrl + "/yanki/filter?phoneNumber=" + id)
      .retrieve()
      .bodyToMono(YankiModel.class);
  }

  public Mono<YankiModel> updateYanki(YankiModel request) {
    return WebClient.create()
      .put()
      .uri(yankiBaseUrl + "/yanki/" + request.getYankiId())
      .bodyValue(request)
      .retrieve()
      .bodyToMono(YankiModel.class);
  }

}
