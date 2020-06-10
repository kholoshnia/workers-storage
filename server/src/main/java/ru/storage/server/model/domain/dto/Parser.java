package ru.storage.server.model.domain.dto;

import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
  public static Double parseDouble(String value) throws ValidationException {
    Double result;

    try {
      result = Double.parseDouble(value);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(e);
    }

    return result;
  }

  public static String parseString(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }

    return value;
  }

  public static LocalDateTime parseLocalDateTime(String value) throws ValidationException {
    LocalDateTime result;

    try {
      result = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException | NullPointerException e) {
      throw new ValidationException(e);
    }

    return result;
  }

  public static Status parseStatus(String value) throws ValidationException {
    Status status;

    try {
      status = Status.valueOf(value);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new ValidationException(e);
    }

    return status;
  }
}
