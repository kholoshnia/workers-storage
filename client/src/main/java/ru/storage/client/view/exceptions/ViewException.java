package ru.storage.client.view.exceptions;

public class ViewException extends Exception {
  public ViewException() {
    super();
  }

  public ViewException(String message) {
    super(message);
  }

  public ViewException(String message, Throwable cause) {
    super(message, cause);
  }

  public ViewException(Throwable cause) {
    super(cause);
  }
}
