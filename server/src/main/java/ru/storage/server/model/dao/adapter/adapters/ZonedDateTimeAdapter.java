package ru.storage.server.model.dao.adapter.adapters;

import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.exceptions.AdapterException;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class ZonedDateTimeAdapter implements Adapter<ZonedDateTime, Timestamp> {
  @Override
  public Timestamp to(ZonedDateTime zonedDateTime) throws AdapterException {
    if (zonedDateTime == null) {
      return null;
    }

    return Timestamp.from(zonedDateTime.toInstant());
  }

  @Override
  public ZonedDateTime from(Timestamp timestamp) throws AdapterException {
    if (timestamp == null) {
      return null;
    }

    return timestamp.toInstant().atZone(ZoneId.systemDefault());
  }
}
