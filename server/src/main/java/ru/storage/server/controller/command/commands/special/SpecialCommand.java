package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.services.exitManager.ExitManager;
import ru.storage.server.controller.command.Command;

import java.util.Locale;
import java.util.Map;

public abstract class SpecialCommand extends Command {
  protected final ExitManager exitManager;

  public SpecialCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, argumentMediator, arguments, locale);
    this.exitManager = exitManager;
  }
}
