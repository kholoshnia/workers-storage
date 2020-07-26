package ru.storage.common.exitManager;

import ru.storage.common.exitManager.exceptions.ExitingException;

public interface ExitListener {
  /**
   * Exits the specified process.
   *
   * @throws ExitingException - in case of exceptions while exiting
   */
  void exit() throws ExitingException;
}
