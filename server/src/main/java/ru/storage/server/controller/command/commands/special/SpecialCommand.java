package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.common.exit.ExitManager;

import java.util.Locale;
import java.util.Map;

public abstract class SpecialCommand extends Command {
  protected final ExitManager exitManager;
  protected final CommandMediator commandMediator;

  public SpecialCommand(
      Configuration configuration,
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, argumentMediator, arguments, locale);
    this.exitManager = exitManager;
    this.commandMediator = commandMediator;
  }
}
