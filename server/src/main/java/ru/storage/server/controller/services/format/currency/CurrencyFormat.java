package ru.storage.server.controller.services.format.currency;

import java.util.Locale;

public abstract class CurrencyFormat {
  public static CurrencyFormat getCurrencyInstance(Locale locale) {
    return new CurrencyCalculator(locale);
  }

  public abstract String format(double value);
}
