package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.exitManager.ExitManager;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class HelpCommand extends SpecialCommand {
  private final String AUTHORIZED_HELP_ANSWER;

  public HelpCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, argumentMediator, arguments, locale, exitManager);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.HelpCommand");

    AUTHORIZED_HELP_ANSWER = resourceBundle.getString("answers.authorizedHelp");
  }

  @Override
  public Response executeCommand() {
    return new Response(Status.OK, AUTHORIZED_HELP_ANSWER);
  }
}
