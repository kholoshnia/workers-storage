package ru.storage.server.controller.services.format.date;

import java.time.ZonedDateTime;
import java.util.Locale;

public abstract class DateFormat {
  public static DateFormat getDateInstance(Locale locale) {
    return new DateFormatter(locale);
  }

  public abstract String format(ZonedDateTime zonedDateTime);
}
