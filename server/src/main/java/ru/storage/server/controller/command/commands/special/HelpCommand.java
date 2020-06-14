package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.command.commands.view.ViewCommand;
import ru.storage.common.exitManager.ExitManager;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class HelpCommand extends SpecialCommand {
  private final String HELP_PREFIX;

  private final String LOGIN_INFO;
  private final String LOGOUT_INFO;
  private final String REGISTER_INFO;
  private final String SHOW_HISTORY_INFO;
  private final String CLEAR_HISTORY_INFO;
  private final String ADD_INFO;
  private final String REMOVE_INFO;
  private final String UPDATE_INFO;
  private final String EXIT_INFO;
  private final String HELP_INFO;
  private final String INFO_INFO;
  private final String SHOW_INFO;

  public HelpCommand(
      Configuration configuration,
      CommandMediator commandMediator,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      ExitManager exitManager) {
    super(configuration, commandMediator, argumentMediator, arguments, locale, exitManager);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.HelpCommand");

    HELP_PREFIX = resourceBundle.getString("prefixes.help");
    LOGIN_INFO = resourceBundle.getString("infos.login");
    LOGOUT_INFO = resourceBundle.getString("infos.logout");
    REGISTER_INFO = resourceBundle.getString("infos.register");
    SHOW_HISTORY_INFO = resourceBundle.getString("infos.show");
    CLEAR_HISTORY_INFO = resourceBundle.getString("infos.clear");
    ADD_INFO = resourceBundle.getString("infos.add");
    REMOVE_INFO = resourceBundle.getString("infos.remove");
    UPDATE_INFO = resourceBundle.getString("infos.update");
    EXIT_INFO = resourceBundle.getString("infos.exit");
    HELP_INFO = resourceBundle.getString("infos.help");
    INFO_INFO = resourceBundle.getString("infos.info");
    SHOW_INFO = resourceBundle.getString("infos.show");
  }

  @Override
  public Response executeCommand() {
    String result =
        ViewCommand.SEPARATOR
            + System.lineSeparator()
            + HELP_PREFIX
            + System.lineSeparator()
            + ViewCommand.SEPARATOR
            + String.format("%s - %s", commandMediator.LOGIN, LOGIN_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.LOGOUT, LOGIN_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.REGISTER, REGISTER_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.SHOW_HISTORY, SHOW_HISTORY_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.CLEAR_HISTORY, CLEAR_HISTORY_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.ADD, ADD_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.REMOVE, REMOVE_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.UPDATE, UPDATE_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.EXIT, EXIT_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.HELP, HELP_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.INFO, INFO_INFO)
            + System.lineSeparator()
            + String.format("%s - %s", commandMediator.SHOW, SHOW_INFO)
            + System.lineSeparator()
            + ViewCommand.SEPARATOR;

    return new Response(Status.OK, result);
  }
}
