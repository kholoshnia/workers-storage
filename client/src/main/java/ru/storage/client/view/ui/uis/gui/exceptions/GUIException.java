package ru.storage.client.view.ui.uis.gui.exceptions;

import ru.storage.client.view.ui.exceptions.UIException;

public final class GUIException extends UIException {
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
