package ru.storage.common.transfer.serizliser.exceptions;

public class DeserializationException extends Exception {
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
