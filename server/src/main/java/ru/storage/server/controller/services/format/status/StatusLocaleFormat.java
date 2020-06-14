package ru.storage.server.controller.services.format.status;

import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.util.Locale;

public final class StatusLocaleFormat extends StatusFormat {
  public StatusLocaleFormat(Locale locale) {
  }

  @Override
  public String format(Status status) {
    return status.toString();
  }
}
