package ru.storage.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.app.concurrent.Executor;
import ru.storage.server.app.concurrent.exceptions.ExecutorServicesException;
import ru.storage.server.app.connection.Connection;
import ru.storage.server.app.connection.ServerConnection;
import ru.storage.server.app.connection.ServerProcessor;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.ServerConnectionException;
import ru.storage.server.controller.Controller;
import ru.storage.server.view.ServerConsole;

public final class Server implements ServerProcessor {
  private final Logger logger;
  private final ServerConsole console;
  private final Executor executor;
  private final Controller controller;
  private final ServerConnection serverConnection;

  public Server(
      ServerConsole console,
      Executor executor,
      Controller controller,
      ServerConnection serverConnection) {
    this.logger = LogManager.getLogger(Server.class);
    this.console = console;
    this.executor = executor;
    this.controller = controller;
    this.serverConnection = serverConnection;
  }

  public void start() {
    /*new Thread(
        () -> {
          try {
            console.start();
          } catch (Throwable e) {
            logger.error(() -> "User interface fatal error, continuing server work.", e);
          }
        })
    .start();*/

    try {
      serverConnection.start();
    } catch (ServerException | ServerConnectionException e) {
      logger.fatal(() -> "Server connection fatal error.", e);
    }
  }

  @Override
  public void process(Connection client) throws ServerException {
    try {
      executor.read(
          () -> {
            Request request = read(client);
            executor.handle(
                () -> {
                  Response response = handle(request);
                  executor.send(() -> send(client, response));
                });
          });
    } catch (ExecutorServicesException e) {
      logger.error(() -> "Error while processing connection with client.", e);
      throw new ServerException(e);
    }
  }

  private Request read(Connection client) {
    try {
      return client.read();
    } catch (ServerConnectionException e) {
      logger.error(() -> "Cannot read request.", e);
      throw new ExecutorServicesException(e);
    }
  }

  private Response handle(Request request) {
    logger.info("Request was handled by {}.", () -> controller.getClass().getSimpleName());
    return controller.handle(request);
  }

  private void send(Connection client, Response response) {
    try {
      client.write(response);
    } catch (ServerConnectionException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new ExecutorServicesException(e);
    }
  }
}
