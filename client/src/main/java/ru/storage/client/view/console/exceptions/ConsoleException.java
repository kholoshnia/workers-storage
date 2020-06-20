package ru.storage.client.view.console.exceptions;

import ru.storage.client.view.exceptions.UserInterfaceException;

public class ConsoleException extends UserInterfaceException {
  public ConsoleException() {
    super();
  }

  public ConsoleException(String message) {
    super(message);
  }

  public ConsoleException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConsoleException(Throwable cause) {
    super(cause);
  }
}
