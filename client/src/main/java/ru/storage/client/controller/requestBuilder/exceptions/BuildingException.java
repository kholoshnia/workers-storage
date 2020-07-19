package ru.storage.client.controller.requestBuilder.exceptions;

public final class BuildingException extends Exception {
  public BuildingException() {
    super();
  }

  public BuildingException(String message) {
    super(message);
  }

  public BuildingException(String message, Throwable cause) {
    super(message, cause);
  }

  public BuildingException(Throwable cause) {
    super(cause);
  }
}
