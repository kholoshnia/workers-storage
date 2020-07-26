package ru.storage.server.controller.services.format.number;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyFormatter
    extends ru.storage.server.controller.services.format.number.NumberFormat {
  private final Logger logger;
  private final NumberFormat numberFormat;

  CurrencyFormatter(Locale locale) {
    logger = LogManager.getLogger(CurrencyFormatter.class);

    if (!locale.equals(Locale.US)) {
      logger.info(() -> "Specified locale is not US, setting formatter as US locale formatter.");
    }

    numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
  }

  @Override
  public String format(Integer value) {
    if (value == null) {
      logger.info(() -> "Got null integer value");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Long value) {
    if (value == null) {
      logger.info(() -> "Got null long value");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Float value) {
    if (value == null) {
      logger.info(() -> "Got null float value");
      return null;
    }

    return numberFormat.format(value);
  }

  @Override
  public String format(Double value) {
    if (value == null) {
      logger.info(() -> "Got null double value");
      return null;
    }

    return numberFormat.format(value);
  }
}
