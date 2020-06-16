package ru.storage.server.controller.command;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;

import java.util.ResourceBundle;

public final class CommandController implements Controller {
  private static final String NO_SUCH_COMMAND_ANSWER;
  private static final String COMMAND_CREATION_ERROR_ANSWER;
  private static final String GOT_NULL_COMMAND_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CommandController");

    NO_SUCH_COMMAND_ANSWER = resourceBundle.getString("answers.noSuchCommand");
    COMMAND_CREATION_ERROR_ANSWER = resourceBundle.getString("answers.commandCreationError");
    GOT_NULL_COMMAND_ANSWER = resourceBundle.getString("answers.gotNullCommand");
  }

  private final Logger logger;
  private final CommandFactoryMediator commandFactoryMediator;

  @Inject
  public CommandController(CommandFactoryMediator commandFactoryMediator) {
    this.logger = LogManager.getLogger(CommandController.class);
    this.commandFactoryMediator = commandFactoryMediator;
  }

  @Override
  public Response handle(Request request) {
    CommandFactory commandFactory = commandFactoryMediator.getCommandFactory(request.getCommand());

    if (commandFactory == null) {
      logger.error(() -> "There is no such command, factory was not created.");
      return new Response(Status.BAD_REQUEST, NO_SUCH_COMMAND_ANSWER);
    }

    Command command;

    try {
      command =
          commandFactory.createCommand(
              request.getCommand(), request.getArguments(), request.getLocale());
    } catch (CommandFactoryException exception) {
      return new Response(Status.INTERNAL_SERVER_ERROR, COMMAND_CREATION_ERROR_ANSWER);
    }

    if (command == null) {
      logger.error(() -> "Got null command form factory.");
      return new Response(Status.INTERNAL_SERVER_ERROR, GOT_NULL_COMMAND_ANSWER);
    }

    return command.executeCommand();
  }
}
