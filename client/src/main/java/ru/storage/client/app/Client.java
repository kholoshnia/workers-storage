package ru.storage.client.app;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.exceptions.ClientException;
import ru.storage.client.view.console.Console;

public final class Client {
  private final Logger logger;
  private final Console console;

  @Inject
  public Client(Console console) {
    logger = LogManager.getLogger(Client.class);
    this.console = console;
  }

  public void start() throws ClientException {
    try {
      console.process();
    } catch (Exception e) {
      logger.fatal("Exception was caught during work of the user interface.");
      throw new ClientException(e);
    }
  }
}
