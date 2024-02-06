package com.vasquez.mstransaction.proxy;

import com.vasquez.mstransaction.proxy.model.AccountResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Account service proxy.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Log4j2
@Service
public class AccountProxy {

  @Value("${microservices.ms-account.base-url}")
  private String accountBaseUrl;

  /**
   * Get account by account number.
   *
   * @param accountNumber account number.
   * @return account response.
   */
  public Mono<AccountResponse> getByAccountNumber(String accountNumber) {
    return WebClient.create()
      .get()
      .uri(accountBaseUrl + "/account/filter?accountNumber=" + accountNumber)
      .retrieve()
      .bodyToMono(AccountResponse.class);
  }

  /**
   * Get account by account id.
   *
   * @param request account
   */
  public Mono<AccountResponse> updateAccount(AccountResponse request) {
    log.info("request: {}", request);
    return WebClient.create()
      .put()
      .uri(accountBaseUrl + "/account/" + request.getAccountId())
      //s.contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .retrieve()
      .bodyToMono(AccountResponse.class);
  }

}
