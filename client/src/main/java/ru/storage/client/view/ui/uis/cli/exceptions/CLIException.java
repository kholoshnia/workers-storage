package ru.storage.client.view.ui.uis.cli.exceptions;

import ru.storage.client.view.ui.exceptions.UIException;

public class CLIException extends UIException {
  public CLIException() {
    super();
  }

  public CLIException(String message) {
    super(message);
  }

  public CLIException(String message, Throwable cause) {
    super(message, cause);
  }

  public CLIException(Throwable cause) {
    super(cause);
  }
}
