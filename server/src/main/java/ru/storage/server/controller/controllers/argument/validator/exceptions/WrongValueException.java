package ru.storage.server.controller.controllers.argument.validator.exceptions;

public final class WrongValueException extends Exception {
  public WrongValueException() {
    super();
  }

  public WrongValueException(String message) {
    super(message);
  }

  public WrongValueException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongValueException(Throwable cause) {
    super(cause);
  }
}
