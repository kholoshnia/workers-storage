package ru.storage.server.model.source.database.exceptions;

import ru.storage.server.model.source.exceptions.DataSourceException;

public final class DatabaseException extends DataSourceException {
  public DatabaseException() {
    super();
  }

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseException(Throwable cause) {
    super(cause);
  }
}
