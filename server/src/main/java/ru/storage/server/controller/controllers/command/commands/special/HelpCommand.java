package ru.storage.server.controller.controllers.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class HelpCommand extends SpecialCommand {
  private static final String INFO_PATTERN = "%-25s- %s";
  private static final String ARGUMENT_PATTERN = "%s <%s>";
  private static final String WORKER_PATTERN = "%s {%s}";

  private final String HELP_PREFIX;

  private final String ENTRY_COMMANDS_PREFIX;
  private final String HISTORY_COMMANDS_PREFIX;
  private final String MODIFICATION_COMMANDS_PREFIX;
  private final String VIEW_COMMANDS_PREFIX;
  private final String SPECIAL_COMMANDS_PREFIX;

  private final String LOGIN_INFO;
  private final String LOGOUT_INFO;
  private final String REGISTER_INFO;

  private final String SHOW_HISTORY_INFO;
  private final String CLEAR_HISTORY_INFO;

  private final String ADD_INFO;
  private final String REMOVE_INFO;
  private final String UPDATE_INFO;

  private final String INFO_INFO;
  private final String SHOW_INFO;

  private final String HELP_INFO;
  private final String EXECUTE_SCRIPT_INFO;
  private final String EXIT_INFO;

  private final String LOGIN_ARGUMENT;
  private final String WORKER_ARGUMENT;
  private final String ID_ARGUMENT;
  private final String PATH_ARGUMENT;

  public HelpCommand(
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
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.HelpCommand");

    HELP_PREFIX = resourceBundle.getString("prefixes.help");

    ENTRY_COMMANDS_PREFIX = resourceBundle.getString("prefixes.entryCommands");
    HISTORY_COMMANDS_PREFIX = resourceBundle.getString("prefixes.historyCommands");
    MODIFICATION_COMMANDS_PREFIX = resourceBundle.getString("prefixes.modificationCommands");
    VIEW_COMMANDS_PREFIX = resourceBundle.getString("prefixes.viewCommands");
    SPECIAL_COMMANDS_PREFIX = resourceBundle.getString("prefixes.specialCommands");

    LOGIN_INFO = resourceBundle.getString("infos.login");
    LOGOUT_INFO = resourceBundle.getString("infos.logout");
    REGISTER_INFO = resourceBundle.getString("infos.register");

    SHOW_HISTORY_INFO = resourceBundle.getString("infos.showHistory");
    CLEAR_HISTORY_INFO = resourceBundle.getString("infos.clearHistory");

    ADD_INFO = resourceBundle.getString("infos.add");
    REMOVE_INFO = resourceBundle.getString("infos.remove");
    UPDATE_INFO = resourceBundle.getString("infos.update");

    INFO_INFO = resourceBundle.getString("infos.info");
    SHOW_INFO = resourceBundle.getString("infos.show");

    HELP_INFO = resourceBundle.getString("infos.help");
    EXECUTE_SCRIPT_INFO = resourceBundle.getString("infos.executeScript");
    EXIT_INFO = resourceBundle.getString("infos.exit");

    LOGIN_ARGUMENT = resourceBundle.getString("arguments.login");
    WORKER_ARGUMENT = resourceBundle.getString("arguments.worker");
    ID_ARGUMENT = resourceBundle.getString("arguments.id");
    PATH_ARGUMENT = resourceBundle.getString("arguments.path");
  }

  private String formEntryCommandsInfo() {
    return ENTRY_COMMANDS_PREFIX
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ARGUMENT_PATTERN, commandMediator.LOGIN, LOGIN_ARGUMENT),
            LOGIN_INFO)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.LOGOUT, LOGOUT_INFO)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.REGISTER, REGISTER_INFO);
  }

  private String formHistoryCommandsInfo() {
    return HISTORY_COMMANDS_PREFIX
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.SHOW_HISTORY, SHOW_HISTORY_INFO)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.CLEAR_HISTORY, CLEAR_HISTORY_INFO);
  }

  private String formModificationCommandsInfo() {
    return MODIFICATION_COMMANDS_PREFIX
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(WORKER_PATTERN, commandMediator.ADD, WORKER_ARGUMENT),
            ADD_INFO)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ARGUMENT_PATTERN, commandMediator.REMOVE, ID_ARGUMENT),
            REMOVE_INFO)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(
                WORKER_PATTERN,
                String.format(ARGUMENT_PATTERN, commandMediator.UPDATE, ID_ARGUMENT),
                WORKER_ARGUMENT),
            UPDATE_INFO);
  }

  private String formViewCommandsInfo() {
    return VIEW_COMMANDS_PREFIX
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.INFO, INFO_INFO)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.SHOW, SHOW_INFO);
  }

  private String formSpecialCommandsInfo() {
    return SPECIAL_COMMANDS_PREFIX
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.HELP, HELP_INFO)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ARGUMENT_PATTERN, commandMediator.EXECUTE_SCRIPT, PATH_ARGUMENT),
            EXECUTE_SCRIPT_INFO)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.EXIT, EXIT_INFO);
  }

  @Override
  public Response executeCommand() {
    String result =
        HELP_PREFIX
            + System.lineSeparator()
            + System.lineSeparator()
            + formEntryCommandsInfo()
            + System.lineSeparator()
            + System.lineSeparator()
            + formHistoryCommandsInfo()
            + System.lineSeparator()
            + System.lineSeparator()
            + formModificationCommandsInfo()
            + System.lineSeparator()
            + System.lineSeparator()
            + formViewCommandsInfo()
            + System.lineSeparator()
            + System.lineSeparator()
            + formSpecialCommandsInfo();

    return new Response(Status.OK, result);
  }
}
