package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions;

public final class FormingException extends Exception {
  public FormingException() {
    super();
  }

  public FormingException(String message) {
    super(message);
  }

  public FormingException(String message, Throwable cause) {
    super(message, cause);
  }

  public FormingException(Throwable cause) {
    super(cause);
  }
}
