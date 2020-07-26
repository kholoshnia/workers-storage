package ru.storage.server.controller.services.format.date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public final class DateFormatter extends DateFormat {
  private final Logger logger;
  private final DateTimeFormatter dateTimeFormatter;

  public DateFormatter(Locale locale) {
    logger = LogManager.getLogger(DateFormatter.class);
    dateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
            .withLocale(locale)
            .withZone(ZoneId.systemDefault());
  }

  @Override
  public String format(ZonedDateTime zonedDateTime) {
    if (zonedDateTime == null) {
      return null;
    }

    String result = dateTimeFormatter.format(zonedDateTime);

    logger.info("Got localized date: {}.", () -> result);
    return result;
  }
}
