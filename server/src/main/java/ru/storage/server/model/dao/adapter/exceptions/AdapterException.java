package ru.storage.server.model.dao.adapter.exceptions;

public final class AdapterException extends Exception {
  public AdapterException() {
    super();
  }

  public AdapterException(String message) {
    super(message);
  }

  public AdapterException(String message, Throwable cause) {
    super(message, cause);
  }

  public AdapterException(Throwable cause) {
    super(cause);
  }
}
