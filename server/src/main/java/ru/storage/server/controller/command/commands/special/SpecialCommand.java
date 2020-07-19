package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.server.controller.command.Command;

import java.util.Locale;
import java.util.Map;

public abstract class SpecialCommand extends Command {
  protected final Locale locale;
  protected final ExitManager exitManager;
  protected final CommandMediator commandMediator;

  public SpecialCommand(
      Configuration configuration,
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, argumentMediator, arguments);
    this.locale = locale;
    this.exitManager = exitManager;
    this.commandMediator = commandMediator;
  }
}
