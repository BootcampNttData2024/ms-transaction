package com.vasquez.mstransaction.business.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * Exception handler.
 *
 * @author Vasquez
 * @version 1.0.
 */
@ControllerAdvice
@AllArgsConstructor
public class HandlerException {
  @ExceptionHandler(ResponseStatusException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleResponseStatusException(ResponseStatusException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getBody().getStatus(), ex.getBody().getTitle(), ex.getReason());
    return Mono.just(ResponseEntity.status(ex.getBody().getStatus()).body(errorResponse));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "An unexpected error occurred");
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
  }
}
