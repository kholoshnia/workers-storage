package ru.storage.server.app.guice.exceptions;

public final class ProvidingException extends Exception {
  public ProvidingException() {
    super();
  }

  public ProvidingException(String message) {
    super(message);
  }

  public ProvidingException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProvidingException(Throwable cause) {
    super(cause);
  }
}
