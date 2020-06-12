package ru.storage.server.app.multithreading.exceptions;

public class ExecutorServicesException extends RuntimeException {
  public ExecutorServicesException() {
    super();
  }

  public ExecutorServicesException(String message) {
    super(message);
  }

  public ExecutorServicesException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutorServicesException(Throwable cause) {
    super(cause);
  }
}
