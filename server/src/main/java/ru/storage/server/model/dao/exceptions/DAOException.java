package ru.storage.server.model.dao.exceptions;

import ru.storage.server.model.source.exceptions.DataSourceException;

public class DAOException extends Exception {
  public DAOException() {
    super();
  }

  public DAOException(String message) {
    super(message);
  }

  public DAOException(String message, Throwable cause) {
    super(message, cause);
  }

  public DAOException(Throwable cause) {
    super(cause);
  }
}
