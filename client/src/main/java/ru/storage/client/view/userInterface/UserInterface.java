package ru.storage.client.view.userInterface;

import ru.storage.client.app.connection.Connection;
import ru.storage.client.view.userInterface.exceptions.UserInterfaceException;

public abstract class UserInterface {
  protected final Connection server;

  public UserInterface(Connection server) {
    this.server = server;
  }

  public abstract void start() throws UserInterfaceException;
}
