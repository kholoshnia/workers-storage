package ru.storage.client.controller.argumentFormer.exceptions;

public final class CancelException extends Exception {
  public CancelException() {
    super();
  }

  public CancelException(String message) {
    super(message);
  }

  public CancelException(String message, Throwable cause) {
    super(message, cause);
  }

  public CancelException(Throwable cause) {
    super(cause);
  }
}
