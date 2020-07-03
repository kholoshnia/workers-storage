package ru.storage.common.managers.exit;

import ru.storage.common.managers.exit.exceptions.ExitingException;

public interface ExitListener {
  void exit() throws ExitingException;
}
