package com.vasquez.mstransaction.proxy;

import com.vasquez.mstransaction.proxy.model.AccountModel;
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
  public Mono<AccountModel> getByAccountNumber(String accountNumber) {
    return WebClient.create()
      .get()
      .uri(accountBaseUrl + "/account/filter?accountNumber=" + accountNumber)
      .retrieve()
      .bodyToMono(AccountModel.class);
  }

  /**
   * Get account
   *
   * @param accountId account
   * @return account response.
   */
  public Mono<AccountModel> getByAccountById(String accountId) {
    return WebClient.create()
      .get()
      .uri(accountBaseUrl + "/account/" + accountId)
      .retrieve()
      .bodyToMono(AccountModel.class);
  }

  /**
   * Get account by account id.
   *
   * @param request account
   */
  public Mono<AccountModel> updateAccount(AccountModel request) {
    log.info("request: {}", request);
    return WebClient.create()
      .put()
      .uri(accountBaseUrl + "/account/" + request.getAccountId())
      //s.contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .retrieve()
      .bodyToMono(AccountModel.class);
  }

}
