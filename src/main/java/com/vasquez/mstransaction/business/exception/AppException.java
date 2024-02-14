package com.vasquez.mstransaction.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * App exception.
 *
 * @author Vasquez
 * @version 1.0.
 */

public class AppException {
  public static ResponseStatusException notFound(String message) {
    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
  }

  public static ResponseStatusException badRequest(String message) {
    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
  }

  public static ResponseStatusException internalServerError(String message) {
    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
