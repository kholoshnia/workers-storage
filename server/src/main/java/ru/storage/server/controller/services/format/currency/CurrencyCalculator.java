package ru.storage.server.controller.services.format.currency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyCalculator extends CurrencyFormat {
  private final Logger logger;
  private final NumberFormat numberFormat;

  CurrencyCalculator(Locale locale) {
    logger = LogManager.getLogger(CurrencyCalculator.class);

    if (!locale.equals(Locale.US)) {
      logger.info(() -> "Specified locale is not US, setting formatter as US locale formatter.");
    }

    numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
  }

  @Override
  public String format(double value) {
    return numberFormat.format(value);
  }
}
