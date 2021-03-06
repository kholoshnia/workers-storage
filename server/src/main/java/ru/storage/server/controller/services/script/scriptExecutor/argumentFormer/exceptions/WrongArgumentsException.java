package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions;

public final class WrongArgumentsException extends Exception {
  public WrongArgumentsException() {
    super();
  }

  public WrongArgumentsException(String message) {
    super(message);
  }

  public WrongArgumentsException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongArgumentsException(Throwable cause) {
    super(cause);
  }
}
