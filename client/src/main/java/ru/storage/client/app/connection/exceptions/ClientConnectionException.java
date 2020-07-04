package ru.storage.client.app.connection.exceptions;

public final class ClientConnectionException extends Exception {
  public ClientConnectionException() {
    super();
  }

  public ClientConnectionException(String message) {
    super(message);
  }

  public ClientConnectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientConnectionException(Throwable cause) {
    super(cause);
  }
}
