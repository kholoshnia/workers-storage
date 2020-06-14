package ru.storage.common.exitManager;

import ru.storage.common.exitManager.exceptions.ExitingException;

public interface ExitListener {
  void exit() throws ExitingException;
}
