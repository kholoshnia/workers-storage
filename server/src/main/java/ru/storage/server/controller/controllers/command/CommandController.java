package ru.storage.server.controller.controllers.command;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.controllers.command.factory.exceptions.UserNotFoundException;
import ru.storage.server.model.domain.history.History;
import ru.storage.server.model.domain.history.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class CommandController implements Controller {
  private static final String COMMAND_CREATION_ERROR_ANSWER;
  private static final String GOT_NULL_COMMAND_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CommandController");

    COMMAND_CREATION_ERROR_ANSWER = resourceBundle.getString("answers.commandCreationError");
    GOT_NULL_COMMAND_ANSWER = resourceBundle.getString("answers.gotNullCommand");
  }

  private final Logger logger;
  private final CommandMediator commandMediator;
  private final ArgumentMediator argumentMediator;
  private final CommandFactoryMediator commandFactoryMediator;
  private final List<String> authCommands;
  private final History history;

  @Inject
  public CommandController(
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      CommandFactoryMediator commandFactoryMediator,
      History history) {
    logger = LogManager.getLogger(CommandController.class);
    this.commandMediator = commandMediator;
    this.argumentMediator = argumentMediator;
    this.commandFactoryMediator = commandFactoryMediator;
    authCommands = initAuthCommandList(argumentMediator);
    this.history = history;
  }

  private List<String> initAuthCommandList(ArgumentMediator argumentMediator) {
    return new ArrayList<String>() {
      {
        add(commandMediator.LOGIN);
        add(commandMediator.REGISTER);
      }
    };
  }

  @Override
  public Response handle(Request request) {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.CommandController", request.getLocale());
    String noSuchCommandAnswer = resourceBundle.getString("answers.noSuchCommand");
    String userNotFound = resourceBundle.getString("answers.userNotFound");

    CommandFactory commandFactory = commandFactoryMediator.getCommandFactory(request.getCommand());

    if (commandFactory == null) {
      logger.error(() -> "There is no such command, factory was not created.");
      return new Response(Status.BAD_REQUEST, noSuchCommandAnswer);
    }

    Command command;

    try {
      command =
          commandFactory.createCommand(
              request.getCommand(),
              request.getArguments(),
              request.getLocale(),
              request.getLogin());
    } catch (UserNotFoundException e) {
      logger.warn(() -> "User was not found", e);
      return new Response(Status.NOT_FOUND, userNotFound);
    } catch (CommandFactoryException e) {
      logger.error("Cannot create command: {}.", (Supplier<?>) request::getCommand, e);
      return new Response(Status.INTERNAL_SERVER_ERROR, COMMAND_CREATION_ERROR_ANSWER);
    }

    if (command == null) {
      logger.error(() -> "Got null command factory.");
      return new Response(Status.INTERNAL_SERVER_ERROR, GOT_NULL_COMMAND_ANSWER);
    }

    Response response = command.executeCommand();

    addToHistory(request, response);
    return response;
  }

  /**
   * Adds new record to the history. NOTE: in case of authentication command replaces password with
   * empty string.
   *
   * @param request client request
   * @param response server response
   */
  private void addToHistory(Request request, Response response) {
    if (authCommands.contains(request.getCommand())) {
      request.getArguments().replace(argumentMediator.USER_PASSWORD, "");
    }

    history.addRecord(new Record(request.getCommand(), request.getArguments(), response));
  }
}
