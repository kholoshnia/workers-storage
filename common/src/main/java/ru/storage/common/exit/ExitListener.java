package ru.storage.common.exit;

import ru.storage.common.exit.exceptions.ExitingException;

public interface ExitListener {
  void exit() throws ExitingException;
}
