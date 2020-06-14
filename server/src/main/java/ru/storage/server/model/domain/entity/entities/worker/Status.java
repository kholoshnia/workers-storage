package ru.storage.server.model.domain.entity.entities.worker;

public enum Status {
  FIRED,
  HIRED,
  PROMOTION;

  public static Status getStatus(String statusString) {
    if (statusString == null) {
      return null;
    }

    Status[] statuses = values();

    for (Status status : statuses) {
      if (status.toString().equals(statusString)) {
        return status;
      }
    }

    return null;
  }
}
