package ru.storage.server.controller.services.hash.exceptions;

public final class HashGeneratorException extends Exception {
  public HashGeneratorException() {
    super();
  }

  public HashGeneratorException(String message) {
    super(message);
  }

  public HashGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  public HashGeneratorException(Throwable cause) {
    super(cause);
  }
}
