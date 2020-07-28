package ru.storage.server.controller.services.format.status;

import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.util.Locale;

public abstract class StatusFormat {
  /**
   * Returns a new instance of the {@link StatusFormatter}.
   *
   * @param locale formatter locale
   * @return new status format instance
   */
  public static StatusFormat getStatusInstance(Locale locale) {
    return new StatusFormatter(locale);
  }

  /**
   * Formats {@link Status} using specified {@link Locale}.
   *
   * @param status status to format
   * @return localized status string
   */
  public abstract String format(Status status);
}
