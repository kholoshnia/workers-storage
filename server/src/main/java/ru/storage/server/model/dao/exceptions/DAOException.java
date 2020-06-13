package ru.storage.server.model.dao.exceptions;

public final class DAOException extends Exception {
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
