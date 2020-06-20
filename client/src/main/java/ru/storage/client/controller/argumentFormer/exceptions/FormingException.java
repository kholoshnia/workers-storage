package ru.storage.client.controller.argumentFormer.exceptions;

import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;

public final class FormingException extends BuildingException {
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
