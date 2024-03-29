package com.vasquez.mstransaction.proxy;

import com.vasquez.mstransaction.proxy.model.CreditModel;
import com.vasquez.mstransaction.proxy.model.DebitCardModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Credit service proxy.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Log4j2
@Service
public class CreditProxy {

  @Value("${microservices.ms-credit.base-url}")
  private String creditBaseUrl;

  /**
   * modify credit.
   *
   * @param request credit
   */
  public Mono<CreditModel> modifyCredit(CreditModel request) {
    log.info("request modifyCredit: {}", request);
    return WebClient.create()
      .patch()
      .uri(creditBaseUrl + "/credit/" + request.getCardNumber())
      .bodyValue(request)
      .retrieve()
      .bodyToMono(CreditModel.class);
  }

  public Mono<DebitCardModel> getDebitCardById(String debitCardId) {
    log.info("request getDebitCardById: {}", debitCardId);
    return WebClient.create()
      .get()
      .uri(creditBaseUrl + "/debit-card/" + debitCardId)
      .retrieve()
      .bodyToMono(DebitCardModel.class);
  }

}
