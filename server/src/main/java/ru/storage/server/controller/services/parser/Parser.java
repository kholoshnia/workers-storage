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
  private final Logger logger;

  public Parser() {
    logger = LogManager.getLogger(Parser.class);
  }

  public Long parseLong(String longString) throws ParserException {
    if (longString == null) {
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
      throw new ParserException(e);
    }

    return result;
  }

  public Float parseFloat(String floatString) throws ParserException {
    if (floatString == null) {
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
      throw new ParserException(e);
    }

    return result;
  }

  public Double parseDouble(String doubleString) throws ParserException {
    if (doubleString == null) {
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
      throw new ParserException(e);
    }

    return result;
  }

  public String parseString(String string) {
    if (string == null || string.trim().isEmpty()) {
      return null;
    }

    return string;
  }

  public Status parseStatus(String statusString, Locale locale) throws ParserException {
    if (statusString == null) {
      logger.info(() -> "Got null status.");
      throw new ParserException();
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
    throw new ParserException();
  }

  public ZonedDateTime parseLocalDateTime(String localDateTimeString) throws ParserException {
    if (localDateTimeString == null) {
      logger.info(() -> "Got null local date time.");
      return null;
    }

    ZonedDateTime result;

    try {
      result = ZonedDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException e) {
      logger.info(
          "Exception was caught during parsing local date time string: \"{}\".",
          (Supplier<?>) () -> localDateTimeString,
          e);
      throw new ParserException(e);
    }

    return result;
  }
}
