package ru.storage.server.controller.services.format.number;

import java.util.Locale;

public abstract class NumberFormat {
  public static NumberFormat getNumberInstance(Locale locale) {
    return new NumberFormatter(locale);
  }

  public static NumberFormat getCurrencyInstance(Locale locale) {
    return new CurrencyFormatter(locale);
  }

  public abstract String format(Integer value);

  public abstract String format(Long value);

  public abstract String format(Float value);

  public abstract String format(Double value);
}
