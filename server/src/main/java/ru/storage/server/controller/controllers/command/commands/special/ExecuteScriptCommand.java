package ru.storage.server.controller.controllers.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ExecuteScriptCommand extends SpecialCommand {
  private final Logger logger;

  public ExecuteScriptCommand(
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
    logger = LogManager.getLogger(ExecuteScriptCommand.class);
  }

  @Override
  public Response executeCommand() {
    List<String> lines = new ArrayList<>(arguments.values());
    Script script = new Script(locale, user, lines);

    logger.info(() -> "Executing script...");
    return scriptExecutor.execute(script);
  }
}
