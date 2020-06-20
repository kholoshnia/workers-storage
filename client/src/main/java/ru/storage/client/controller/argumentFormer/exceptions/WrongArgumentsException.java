package ru.storage.client.controller.argumentFormer.exceptions;

import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;

public final class WrongArgumentsException extends BuildingException {
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
