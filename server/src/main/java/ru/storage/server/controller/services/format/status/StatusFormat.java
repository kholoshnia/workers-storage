package ru.storage.server.controller.services.format.status;

import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.util.Locale;

public abstract class StatusFormat {
  public static StatusFormat getStatusInstance(Locale locale) {
    return new StatusLocaleFormat(locale);
  }

  public abstract String format(Status value);
}
