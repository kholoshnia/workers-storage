package ru.storage.server.controller.services.format.date;

import java.time.ZonedDateTime;
import java.util.Locale;

public abstract class DateFormat {
  /**
   * Returns new instance of the {@link DateFormatter}.
   *
   * @param locale formatter locale
   * @return new date format instance
   */
  public static DateFormat getDateInstance(Locale locale) {
    return new DateFormatter(locale);
  }

  /**
   * Formats {@link ZonedDateTime} using specified {@link Locale}.
   *
   * @param zonedDateTime status to format
   * @return localized status string
   */
  public abstract String format(ZonedDateTime zonedDateTime);
}
