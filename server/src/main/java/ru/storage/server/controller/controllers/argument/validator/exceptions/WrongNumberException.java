package ru.storage.server.controller.controllers.argument.validator.exceptions;

public final class WrongNumberException extends Exception {
  public WrongNumberException() {
    super();
  }

  public WrongNumberException(String message) {
    super(message);
  }

  public WrongNumberException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongNumberException(Throwable cause) {
    super(cause);
  }
}
