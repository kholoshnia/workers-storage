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
      "--------------------------------------------------< HELP >--------------------------------------------------";
  public static final String SEPARATOR =
      "------------------------------------------------------------------------------------------------------------";
  private static final String NO_ARGUMENTS = "%-20s- %s";
  private static final String WITH_ARGUMENTS = "%-20s <%s> - %s";

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

  private final String LOGIN_ARGUMENT;
  private final String REMOVE_ARGUMENT;
  private final String UPDATE_ARGUMENT;

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

    LOGIN_ARGUMENT = resourceBundle.getString("arguments.login");
    REMOVE_ARGUMENT = resourceBundle.getString("arguments.remove");
    UPDATE_ARGUMENT = resourceBundle.getString("arguments.update");
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
            + String.format(WITH_ARGUMENTS, commandMediator.LOGIN, LOGIN_ARGUMENT, LOGIN_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.LOGOUT, LOGOUT_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.REGISTER, REGISTER_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.SHOW_HISTORY, SHOW_HISTORY_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.CLEAR_HISTORY, CLEAR_HISTORY_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.ADD, ADD_INFO)
            + System.lineSeparator()
            + String.format(WITH_ARGUMENTS, commandMediator.REMOVE, REMOVE_ARGUMENT, REMOVE_INFO)
            + System.lineSeparator()
            + String.format(WITH_ARGUMENTS, commandMediator.UPDATE, UPDATE_ARGUMENT, UPDATE_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.EXIT, EXIT_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.HELP, HELP_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.INFO, INFO_INFO)
            + System.lineSeparator()
            + String.format(NO_ARGUMENTS, commandMediator.SHOW, SHOW_INFO)
            + System.lineSeparator()
            + SEPARATOR;

    return new Response(Status.OK, result);
  }
}
