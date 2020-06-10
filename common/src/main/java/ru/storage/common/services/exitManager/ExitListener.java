package ru.storage.common.services.exitManager;

import ru.storage.common.services.exitManager.exceptions.ExitingException;

public interface ExitListener {
  void exit() throws ExitingException;
}
