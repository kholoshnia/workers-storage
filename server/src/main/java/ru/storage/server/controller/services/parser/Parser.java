package ru.storage.server.controller.services.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Parser {
  private final Logger logger;

  public Parser() {
    logger = LogManager.getLogger(Parser.class);
  }

  public Double parseDouble(String doubleString) throws ParserException {
    Double result;

    try {
      result = Double.parseDouble(doubleString);
    } catch (NumberFormatException | NullPointerException e) {
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

  public Status parseStatus(String statusString) throws ParserException {
    Status status;

    try {
      status = Status.valueOf(statusString);
    } catch (IllegalArgumentException | NullPointerException e) {
      logger.info(
          "Exception was caught during parsing status string: \"{}\".",
          (Supplier<?>) () -> statusString,
          e);
      throw new ParserException(e);
    }

    return status;
  }

  public ZonedDateTime parseLocalDateTime(String localDateTimeString) throws ParserException {
    ZonedDateTime result;

    try {
      result = ZonedDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException | NullPointerException e) {
      logger.info(
          "Exception was caught during parsing local date time string: \"{}\".",
          (Supplier<?>) () -> localDateTimeString,
          e);
      throw new ParserException(e);
    }

    return result;
  }
}
