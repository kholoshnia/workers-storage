package ru.storage.server.app.connection.selector.exceptions;

public class ServerConnectionException extends Exception {
  public ServerConnectionException() {
    super();
  }

  public ServerConnectionException(String message) {
    super(message);
  }

  public ServerConnectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServerConnectionException(Throwable cause) {
    super(cause);
  }
}
