package ru.storage.server.controller.services.format.number;

import java.util.Locale;

public abstract class NumberFormat {
  /**
   * Returns new instance of the {@link NumberFormatter}.
   *
   * @param locale formatter locale
   * @return new number format instance
   */
  public static NumberFormat getNumberInstance(Locale locale) {
    return new NumberFormatter(locale);
  }

  /**
   * Returns new instance of the {@link CurrencyFormatter}.
   *
   * @param locale formatter locale
   * @return new currency format instance
   */
  public static NumberFormat getCurrencyInstance(Locale locale) {
    return new CurrencyFormatter(locale);
  }

  /**
   * Formats {@link Integer} using specified {@link Locale}.
   *
   * @param value integer to format
   * @return localized integer string
   */
  public abstract String format(Integer value);

  /**
   * Formats {@link Long} using specified {@link Locale}.
   *
   * @param value long to format
   * @return localized long string
   */
  public abstract String format(Long value);

  /**
   * Formats {@link Float} using specified {@link Locale}.
   *
   * @param value float to format
   * @return localized float string
   */
  public abstract String format(Float value);

  /**
   * Formats {@link Double} using specified {@link Locale}.
   *
   * @param value double to format
   * @return localized double string
   */
  public abstract String format(Double value);
}
