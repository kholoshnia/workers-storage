package ru.storage.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.view.userInterface.UserInterface;
import ru.storage.client.view.userInterface.exceptions.UserInterfaceException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.app.connection.ConnectionProcessor;
import ru.storage.server.app.connection.ServerConnection;
import ru.storage.server.app.connection.ServerProcessor;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.multithreading.Executors;
import ru.storage.server.app.multithreading.exceptions.ExecutorServicesException;
import ru.storage.server.controller.Controller;

public final class Server implements ConnectionProcessor {
  private final Logger logger;
  private final UserInterface userInterface;
  private final Executors executors;
  private final Controller controller;
  private final ServerProcessor serverProcessor;

  public Server(
      UserInterface userInterface,
      Executors executors,
      Controller controller,
      ServerProcessor serverProcessor) {
    this.logger = LogManager.getLogger(Server.class);
    this.userInterface = userInterface;
    this.executors = executors;
    this.controller = controller;
    this.serverProcessor = serverProcessor;
  }

  public void start() {
    new Thread(
            () -> {
              try {
                userInterface.start();
              } catch (UserInterfaceException e) {
                logger.fatal(() -> "User interface fatal error, continuing server work.", e);
              }
            })
        .start();

    try {
      serverProcessor.start();
    } catch (ServerException e) {
      logger.fatal(() -> "Server connection fatal error.", e);
    }
  }

  @Override
  public void process(ServerConnection client) {
    try {
      executors.read(
          () -> {
            Request request;

            try {
              request = client.read();
            } catch (ServerException e) {
              logger.error(() -> "Cannot read request.", e);
              throw new ExecutorServicesException(e);
            }

            executors.handle(
                () -> {
                  Response response = controller.handle(request);

                  executors.send(
                      () -> {
                        try {
                          client.write(response);
                        } catch (ServerException e) {
                          logger.error(() -> "Cannot write response.", e);
                          throw new ExecutorServicesException(e);
                        }
                      });
                });
          });
    } catch (ExecutorServicesException e) {
      logger.error(() -> "Error while processing client.", e);
    }
  }
}
