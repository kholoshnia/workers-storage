package ru.storage.server.controller.services.format.number;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public final class NumberFormatter extends NumberFormat {
  private static final Logger logger = LogManager.getLogger(NumberFormatter.class);

  private final java.text.NumberFormat numberFormat;

  NumberFormatter(Locale locale) {
    numberFormat = java.text.NumberFormat.getNumberInstance(locale);
  }

  @Override
  public String format(Integer value) {
    if (value == null) {
      logger.info(() -> "Got null integer.");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Long value) {
    if (value == null) {
      logger.info(() -> "Got null long.");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Float value) {
    if (value == null) {
      logger.info(() -> "Got null float.");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Double value) {
    if (value == null) {
      logger.info(() -> "Got null double.");
      return null;
    }

    return numberFormat.format(value);
  }
}
