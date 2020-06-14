package ru.storage.client.view.userInterface;

import ru.storage.client.view.userInterface.exceptions.UserInterfaceException;

public interface UserInterface {
  void start() throws UserInterfaceException;

  void show(String string);
}
