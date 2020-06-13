package ru.storage.server.controller.services.exitManager.exceptions;

public class ExitingException extends Exception {
  public ExitingException() {
    super();
  }

  public ExitingException(String message) {
    super(message);
  }

  public ExitingException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExitingException(Throwable cause) {
    super(cause);
  }
}
