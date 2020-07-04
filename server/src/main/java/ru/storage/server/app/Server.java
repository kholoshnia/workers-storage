package ru.storage.server.app;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.app.concurrent.ExecutorService;
import ru.storage.server.app.concurrent.exceptions.ExecutorServicesException;
import ru.storage.server.app.connection.ClientWorker;
import ru.storage.server.app.connection.ServerConnection;
import ru.storage.server.app.connection.ServerProcessor;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.SelectorException;
import ru.storage.server.controller.Controller;

import java.util.List;
import java.util.ResourceBundle;

public final class Server implements ServerProcessor {
  public static final String NO_RESPONSE_FROM_HANDLERS_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Server");

    NO_RESPONSE_FROM_HANDLERS_ANSWER = resourceBundle.getString("answers.noResponseFromHandlers");
  }

  private final Logger logger;
  private final ExecutorService executorService;
  private final List<Controller> controllers;
  private final ServerConnection serverConnection;

  @Inject
  public Server(
      ExecutorService executorService,
      List<Controller> controllers,
      ServerConnection serverConnection) {
    this.logger = LogManager.getLogger(Server.class);
    this.executorService = executorService;
    this.controllers = controllers;
    this.serverConnection = serverConnection;
  }

  public void start() throws ServerException {
    try {
      serverConnection.process();
    } catch (SelectorException e) {
      logger.fatal(() -> "Server processing fatal error.", e);
      throw new ServerException(e);
    }
  }

  @Override
  public void process(ClientWorker clientWorker) throws ServerException {
    try {
      executorService.read(
          () -> {
            Request request = read(clientWorker);
            executorService.handle(
                () -> {
                  Response response = handle(request);
                  executorService.send(() -> send(clientWorker, response));
                });
          });
    } catch (ExecutorServicesException e) {
      logger.error(() -> "Error while processing client.", e);
      throw new ServerException(e);
    }
  }

  private Request read(ClientWorker clientWorker) {
    try {
      return clientWorker.read();
    } catch (SelectorException e) {
      logger.error(() -> "Cannot read request.", e);
      throw new ExecutorServicesException(e);
    }
  }

  protected Response handle(Request request) {
    for (Controller controller : controllers) {
      Response response = controller.handle(request);
      if (response != null) {
        return response;
      }
    }

    return new Response(Status.INTERNAL_SERVER_ERROR, NO_RESPONSE_FROM_HANDLERS_ANSWER);
  }

  private void send(ClientWorker clientWorker, Response response) {
    try {
      clientWorker.write(response);
    } catch (SelectorException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new ExecutorServicesException(e);
    }
  }
}
