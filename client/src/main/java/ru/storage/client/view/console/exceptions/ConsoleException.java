package ru.storage.client.view.console.exceptions;

import ru.storage.client.view.exceptions.ViewException;

public final class ConsoleException extends ViewException {
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
