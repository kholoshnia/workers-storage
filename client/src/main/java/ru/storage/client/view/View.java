package ru.storage.client.view;

import ru.storage.common.exitManager.exceptions.ExitingException;

public interface View {
  void process() throws ExitingException;
}
