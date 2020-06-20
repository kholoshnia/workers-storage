package ru.storage.client.view.exceptions;

public class UserInterfaceException extends Exception {
  public UserInterfaceException() {
    super();
  }

  public UserInterfaceException(String message) {
    super(message);
  }

  public UserInterfaceException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserInterfaceException(Throwable cause) {
    super(cause);
  }
}
