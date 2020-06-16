package ru.storage.client.view.userInterface.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.Connection;
import ru.storage.client.view.userInterface.UserInterface;
import ru.storage.client.view.userInterface.console.exceptions.ConsoleException;

public final class Console extends UserInterface {
  private final Logger logger;

  public Console(Connection connection) {
    super(connection);
    this.logger = LogManager.getLogger(Console.class);
  }

  public void start() throws ConsoleException {}
}
