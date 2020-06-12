package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.services.exitManager.ExitManager;
import ru.storage.common.transfer.response.Response;

import java.util.Locale;
import java.util.Map;

public class HelpCommand extends SpecialCommand {
  public HelpCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, argumentMediator, arguments, locale, exitManager);
  }

  @Override
  public Response executeCommand() {
    return null;
  }
}
