package ru.storage.server.controller.controllers.command.factory.exceptions;

public final class UserNotFoundException extends CommandFactoryException {
  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserNotFoundException(Throwable cause) {
    super(cause);
  }
}
