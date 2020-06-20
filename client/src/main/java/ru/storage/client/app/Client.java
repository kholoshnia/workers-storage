package ru.storage.client.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.exceptions.ClientException;
import ru.storage.client.view.UserInterface;

public final class Client {
  private final Logger logger;
  private final UserInterface userInterface;

  public Client(UserInterface userInterface) {
    this.logger = LogManager.getLogger(Client.class);
    this.userInterface = userInterface;
  }

  public void start() throws ClientException {
    try {
      userInterface.start();
    } catch (Throwable e) {
      logger.fatal("Error while work of user interface.");
      throw new ClientException(e);
    }
  }
}
