package ru.storage.client.view.userInterface.userInterfaces.gui.exceptions;

import ru.storage.client.view.userInterface.exceptions.UserInterfaceException;

public final class GUIException extends UserInterfaceException {
  public GUIException() {
    super();
  }

  public GUIException(String message) {
    super(message);
  }

  public GUIException(String message, Throwable cause) {
    super(message, cause);
  }

  public GUIException(Throwable cause) {
    super(cause);
  }
}
