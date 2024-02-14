package com.vasquez.mstransaction.proxy;

import com.vasquez.mstransaction.proxy.model.ProductModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Product proxy.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class ProductProxy {

  @Value("${microservices.ms-product.base-url}")
  private String productBaseUrl;

  /**
   * Count product by client id.
   *
   * @param id client id
   * @return Mono Integer
   */
  public Mono<ProductModel> getProductById(String id) {
    return WebClient.create()
      .get()
      .uri(productBaseUrl + "/product/" + id)
      .retrieve()
      .bodyToMono(ProductModel.class);
  }

}
