package ru.storage.common.serizliser.exceptions;

public final class DeserializationException extends Exception {
  public DeserializationException() {
    super();
  }

  public DeserializationException(String message) {
    super(message);
  }

  public DeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeserializationException(Throwable cause) {
    super(cause);
  }
}
