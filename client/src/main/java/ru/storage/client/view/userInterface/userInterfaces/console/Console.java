package ru.storage.client.view.userInterface.userInterfaces.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.view.userInterface.userInterfaces.console.exceptions.ConsoleException;

public final class Console {
  private final Logger logger;

  public Console() {
    this.logger = LogManager.getLogger(Console.class);
  }

  public void start() throws ConsoleException {}
}
