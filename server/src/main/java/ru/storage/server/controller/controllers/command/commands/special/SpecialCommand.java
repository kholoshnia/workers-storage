package ru.storage.server.controller.controllers.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.Locale;
import java.util.Map;

public abstract class SpecialCommand extends Command {
  protected final CommandMediator commandMediator;
  protected final User user;
  protected final Locale locale;
  protected final ExitManager exitManager;
  protected final ScriptExecutor scriptExecutor;

  public SpecialCommand(
      Configuration configuration,
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      User user,
      Locale locale,
      ExitManager exitManager,
      ScriptExecutor scriptExecutor) {
    super(configuration, argumentMediator, arguments);
    this.commandMediator = commandMediator;
    this.user = user;
    this.locale = locale;
    this.exitManager = exitManager;
    this.scriptExecutor = scriptExecutor;
  }
}
