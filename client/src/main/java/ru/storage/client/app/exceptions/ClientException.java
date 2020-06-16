package ru.storage.client.app.exceptions;

public final class ClientException extends Exception {
  public ClientException() {
    super();
  }

  public ClientException(String message) {
    super(message);
  }

  public ClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientException(Throwable cause) {
    super(cause);
  }
}
