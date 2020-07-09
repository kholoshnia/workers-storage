package ru.storage.server.controller.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class HelpCommand extends SpecialCommand {
  public static final String BEGINNING =
      "---------------------------------------------< HELP >---------------------------------------------";
  public static final String SEPARATOR =
      "--------------------------------------------------------------------------------------------------";
  private static final String PATTERN = "%-20s- %s";

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

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.HelpCommand");

    HELP_PREFIX = resourceBundle.getString("prefixes.help");
    LOGIN_INFO = resourceBundle.getString("infos.login");
    LOGOUT_INFO = resourceBundle.getString("infos.logout");
    REGISTER_INFO = resourceBundle.getString("infos.register");
    SHOW_HISTORY_INFO = resourceBundle.getString("infos.showHistory");
    CLEAR_HISTORY_INFO = resourceBundle.getString("infos.clearHistory");
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
        BEGINNING
            + System.lineSeparator()
            + HELP_PREFIX
            + System.lineSeparator()
            + SEPARATOR
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.LOGIN, LOGIN_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.LOGOUT, LOGOUT_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.REGISTER, REGISTER_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.SHOW_HISTORY, SHOW_HISTORY_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.CLEAR_HISTORY, CLEAR_HISTORY_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.ADD, ADD_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.REMOVE, REMOVE_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.UPDATE, UPDATE_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.EXIT, EXIT_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.HELP, HELP_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.INFO, INFO_INFO)
            + System.lineSeparator()
            + String.format(PATTERN, commandMediator.SHOW, SHOW_INFO)
            + System.lineSeparator()
            + SEPARATOR;

    return new Response(Status.OK, result);
  }
}
