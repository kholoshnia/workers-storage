package ru.storage.server.app.connection.selector.exceptions;

public class SelectorException extends Exception {
  public SelectorException() {
    super();
  }

  public SelectorException(String message) {
    super(message);
  }

  public SelectorException(String message, Throwable cause) {
    super(message, cause);
  }

  public SelectorException(Throwable cause) {
    super(cause);
  }
}
