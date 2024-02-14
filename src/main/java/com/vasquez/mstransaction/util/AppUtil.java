package com.vasquez.mstransaction.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * App utility.
 *
 * @author Vasquez
 * @version 1.0.
 */
public class AppUtil {

  /**
   * String to LocalDateTime.
   *
   * @param date string
   * @return LocalDateTime
   */
  public static LocalDateTime stringToLocalDateTime(String date) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(date, format);
  }

  /**
   * LocalDateTime to String.
   *
   * @param dateTime LocalDateTime
   * @return String
   */
  public static String localDateTimeToString(LocalDateTime dateTime) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateTime.format(format);
  }

}
