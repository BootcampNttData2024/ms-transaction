package com.vasquez.mstransaction.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountProxy {

    @Value("${microservices.ms-account.base-url}")
    private String accountBaseUrl;

    public Mono<AccountResponse> getByAccountNumber(String accountNumber) {
        return WebClient.create()
                .get()
                .uri(accountBaseUrl + "/account/filter?accountNumber=" + accountNumber)
                .retrieve()
                .bodyToMono(AccountResponse.class);
    }

    public Mono<AccountResponse> updateAccount(AccountResponse request) {
        System.out.println("test " + request.toString());
        return WebClient.create()
                .put()
                .uri(accountBaseUrl + "/account/" + request.getAccountId())
                //s.contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AccountResponse.class);
    }

}
