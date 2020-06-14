package ru.storage.client.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.ClientConnection;
import ru.storage.client.app.connection.ClientProcessor;
import ru.storage.client.app.connection.ConnectionProcessor;
import ru.storage.client.app.connection.exceptions.ClientException;
import ru.storage.client.view.userInterface.UserInterface;
import ru.storage.client.view.userInterface.exceptions.UserInterfaceException;

public final class Client implements ConnectionProcessor {
  private final Logger logger;
  private final UserInterface userInterface;
  private final ClientProcessor clientProcessor;

  public Client(UserInterface userInterface, ClientProcessor clientProcessor) {
    this.logger = LogManager.getLogger(Client.class);
    this.userInterface = userInterface;
    this.clientProcessor = clientProcessor;
  }

  public void start() {
    new Thread(
            () -> {
              try {
                userInterface.start();
              } catch (UserInterfaceException e) {
                logger.fatal(() -> "User interface fatal error.", e);
              }
            })
        .start();

    try {
      clientProcessor.start();
    } catch (ClientException e) {
      logger.fatal(() -> "Client connection fatal error.", e);
    }
  }

  @Override
  public void process(ClientConnection server) throws ClientException {}
}
