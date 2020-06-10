package ru.storage.server.model.source.exceptions;

public class DataSourceException extends Exception {
  public DataSourceException() {
    super();
  }

  public DataSourceException(String message) {
    super(message);
  }

  public DataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataSourceException(Throwable cause) {
    super(cause);
  }
}
