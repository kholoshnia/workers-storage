package ru.storage.server.controller.services.format.date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public final class DateFormatter extends DateFormat {
  private static final Logger logger = LogManager.getLogger(DateFormatter.class);

  private final DateTimeFormatter dateTimeFormatter;

  public DateFormatter(Locale locale) {
    dateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
            .withLocale(locale)
            .withZone(ZoneId.systemDefault());
  }

  @Override
  public String format(ZonedDateTime zonedDateTime) {
    if (zonedDateTime == null) {
      logger.info(() -> "Got null zoned date time.");
      return null;
    }

    String result = dateTimeFormatter.format(zonedDateTime);

    logger.info("Got localized zoned date time: {}.", () -> result);
    return result;
  }
}
