package ru.storage.server.controller.services.exitManager;

import ru.storage.server.controller.services.exitManager.exceptions.ExitingException;

public interface ExitListener {
  void exit() throws ExitingException;
}
