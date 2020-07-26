package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public enum Status {
  FIRED,
  HIRED,
  PROMOTION;

  public static Status getStatus(String statusString) throws ValidationException {
    if (statusString == null) {
      return null;
    }

    Status[] statuses = values();

    for (Status status : statuses) {
      if (status.name().equals(statusString)) {
        return status;
      }
    }

    throw new ValidationException();
  }
}
