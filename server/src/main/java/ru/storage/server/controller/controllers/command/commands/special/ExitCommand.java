package ru.storage.server.controller.controllers.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.exitManager.exceptions.ExitingException;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ExitCommand extends SpecialCommand {
  private final String EXIT_ERROR_ANSWER;
  private final String EXIT_SUCCESSFULLY_ANSWER;

  private final Logger logger;

  public ExitCommand(
      Configuration configuration,
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      User user,
      Locale locale,
      ExitManager exitManager,
      ScriptExecutor scriptExecutor) {
    super(
        configuration,
        commandMediator,
        argumentMediator,
        arguments,
        user,
        locale,
        exitManager,
        scriptExecutor);
    logger = LogManager.getLogger(ExitCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ExitCommand", locale);

    EXIT_ERROR_ANSWER = resourceBundle.getString("answers.exitError");
    EXIT_SUCCESSFULLY_ANSWER = resourceBundle.getString("answers.exitSuccessfully");
  }

  @Override
  public Response executeCommand() {
    try {
      exitManager.exit();
    } catch (ExitingException e) {
      logger.fatal(() -> "Cannot exit.", e);
      return new Response(Status.INTERNAL_SERVER_ERROR, EXIT_ERROR_ANSWER);
    }

    return new Response(Status.OK, EXIT_SUCCESSFULLY_ANSWER);
  }
}
