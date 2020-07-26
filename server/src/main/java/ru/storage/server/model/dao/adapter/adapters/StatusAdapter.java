package ru.storage.server.model.dao.adapter.adapters;

import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.exceptions.AdapterException;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public final class StatusAdapter implements Adapter<Status, String> {
  @Override
  public String to(Status status) {
    if (status == null) {
      return null;
    }

    return status.name();
  }

  @Override
  public Status from(String string) throws AdapterException {
    if (string == null) {
      return null;
    }

    Status status;

    try {
      status = Status.getStatus(string);
    } catch (ValidationException e) {
      throw new AdapterException(e);
    }

    return status;
  }
}
