package ru.storage.server.controller.services.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Parser {
  private static final String PARSE_LONG_EXCEPTION;
  private static final String PARSE_FLOAT_EXCEPTION;
  private static final String PARSE_DOUBLE_EXCEPTION;
  private static final String PARSE_STATUS_EXCEPTION;
  private static final String PARSE_ZONED_DATE_TIME_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Parser");

    PARSE_LONG_EXCEPTION = resourceBundle.getString("exceptions.parseLong");
    PARSE_FLOAT_EXCEPTION = resourceBundle.getString("exceptions.parseFloat");
    PARSE_DOUBLE_EXCEPTION = resourceBundle.getString("exceptions.parseDouble");
    PARSE_STATUS_EXCEPTION = resourceBundle.getString("exceptions.parseStatus");
    PARSE_ZONED_DATE_TIME_EXCEPTION = resourceBundle.getString("exceptions.parseZonedDateTime");
  }

  private final Logger logger;

  public Parser() {
    logger = LogManager.getLogger(Parser.class);
  }

  public Long parseLong(String longString) throws ParserException {
    if (longString == null || longString.isEmpty()) {
      logger.info(() -> "Got null long.");
      return null;
    }

    long result;

    try {
      result = Long.parseLong(longString);
    } catch (NumberFormatException e) {
      logger.info(
          "Exception was caught during parsing long string: \"{}\".",
          (Supplier<?>) () -> longString,
          e);
      throw new ParserException(PARSE_LONG_EXCEPTION, e);
    }

    return result;
  }

  public Float parseFloat(String floatString) throws ParserException {
    if (floatString == null || floatString.isEmpty()) {
      logger.info(() -> "Got null float.");
      return null;
    }

    float result;

    try {
      result = Float.parseFloat(floatString);
    } catch (NumberFormatException e) {
      logger.info(
          "Exception was caught during parsing float string: \"{}\".",
          (Supplier<?>) () -> floatString,
          e);
      throw new ParserException(PARSE_FLOAT_EXCEPTION, e);
    }

    return result;
  }

  public Double parseDouble(String doubleString) throws ParserException {
    if (doubleString == null || doubleString.isEmpty()) {
      logger.info(() -> "Got null double.");
      return null;
    }

    double result;

    try {
      result = Double.parseDouble(doubleString);
    } catch (NumberFormatException e) {
      logger.info(
          "Exception was caught during parsing double string: \"{}\".",
          (Supplier<?>) () -> doubleString,
          e);
      throw new ParserException(PARSE_DOUBLE_EXCEPTION, e);
    }

    return result;
  }

  public String parseString(String string) {
    if (string == null || string.isEmpty()) {
      return null;
    }

    return string;
  }

  public Status parseStatus(String statusString, Locale locale) throws ParserException {
    if (statusString == null || statusString.isEmpty()) {
      logger.info(() -> "Got null status.");
      return null;
    }

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.StatusFormat", locale);

    Status[] statuses = Status.values();
    Enumeration<String> keys = resourceBundle.getKeys();

    for (Status value : statuses) {
      while (keys.hasMoreElements()) {
        if (resourceBundle.getString(keys.nextElement()).equals(statusString)) {
          return value;
        }
      }
    }

    logger.info("Got wrong status: {}.", () -> statusString);
    throw new ParserException(PARSE_STATUS_EXCEPTION);
  }

  public ZonedDateTime parseLocalDateTime(String zonedDateTimeString) throws ParserException {
    if (zonedDateTimeString == null || zonedDateTimeString.isEmpty()) {
      logger.info(() -> "Got null zoned date time.");
      return null;
    }

    ZonedDateTime result;

    try {
      result = ZonedDateTime.parse(zonedDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      logger.info(
          "Exception was caught during parsing zoned date time string: \"{}\".",
          (Supplier<?>) () -> zonedDateTimeString,
          e);
      throw new ParserException(PARSE_ZONED_DATE_TIME_EXCEPTION, e);
    }

    return result;
  }
}
