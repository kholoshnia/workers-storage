package ru.storage.server.controller.services.format.currency;

import java.text.NumberFormat;
import java.util.Locale;

// TODO: Implement using Google Finance API.
public final class CurrencyCalculator extends CurrencyFormat {
  private final NumberFormat numberFormat;

  CurrencyCalculator(Locale locale) {
    numberFormat = NumberFormat.getCurrencyInstance(locale);
  }

  @Override
  public String format(double value) {
    return numberFormat.format(value);
  }
}
