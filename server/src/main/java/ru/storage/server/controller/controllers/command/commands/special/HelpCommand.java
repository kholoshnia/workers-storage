package ru.storage.server.controller.controllers.command.commands.special;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private static final Logger logger = LogManager.getLogger(HelpCommand.class);

  private static final String INFO_PATTERN = "%-25s- %s";
  private static final String ARGUMENT_PATTERN = "%s <%s>";
  private static final String ADDITIONAL_PATTERN = "%s {%s}";

  private final String helpPrefix;

  private final String entryCommandsPrefix;
  private final String historyCommandsPrefix;
  private final String modificationCommandsPrefix;
  private final String viewCommandsPrefix;
  private final String specialCommandsPrefix;

  private final String loginInfo;
  private final String logoutInfo;
  private final String registerInfo;

  private final String showHistoryInfo;
  private final String clearHistoryInfo;

  private final String addInfo;
  private final String removeInfo;
  private final String updateInfo;

  private final String infoInfo;
  private final String showInfo;

  private final String helpInfo;
  private final String executeScriptInfo;
  private final String exitInfo;

  private final String loginArgument;
  private final String passwordArgument;
  private final String userArgument;
  private final String workerArgument;
  private final String idArgument;
  private final String pathArgument;

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

    helpPrefix = resourceBundle.getString("prefixes.help");

    entryCommandsPrefix = resourceBundle.getString("prefixes.entryCommands");
    historyCommandsPrefix = resourceBundle.getString("prefixes.historyCommands");
    modificationCommandsPrefix = resourceBundle.getString("prefixes.modificationCommands");
    viewCommandsPrefix = resourceBundle.getString("prefixes.viewCommands");
    specialCommandsPrefix = resourceBundle.getString("prefixes.specialCommands");

    loginInfo = resourceBundle.getString("infos.login");
    logoutInfo = resourceBundle.getString("infos.logout");
    registerInfo = resourceBundle.getString("infos.register");

    showHistoryInfo = resourceBundle.getString("infos.showHistory");
    clearHistoryInfo = resourceBundle.getString("infos.clearHistory");

    addInfo = resourceBundle.getString("infos.add");
    removeInfo = resourceBundle.getString("infos.remove");
    updateInfo = resourceBundle.getString("infos.update");

    infoInfo = resourceBundle.getString("infos.info");
    showInfo = resourceBundle.getString("infos.show");

    helpInfo = resourceBundle.getString("infos.help");
    executeScriptInfo = resourceBundle.getString("infos.executeScript");
    exitInfo = resourceBundle.getString("infos.exit");

    loginArgument = resourceBundle.getString("arguments.login");
    passwordArgument = resourceBundle.getString("arguments.password");
    userArgument = resourceBundle.getString("arguments.user");
    workerArgument = resourceBundle.getString("arguments.worker");
    idArgument = resourceBundle.getString("arguments.id");
    pathArgument = resourceBundle.getString("arguments.path");
  }

  private String formEntryCommandsInfo() {
    return entryCommandsPrefix
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(
                ADDITIONAL_PATTERN,
                String.format(ARGUMENT_PATTERN, commandMediator.login, loginArgument),
                passwordArgument),
            loginInfo)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.logout, logoutInfo)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ADDITIONAL_PATTERN, commandMediator.register, userArgument),
            registerInfo);
  }

  private String formHistoryCommandsInfo() {
    return historyCommandsPrefix
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.showHistory, showHistoryInfo)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.clearHistory, clearHistoryInfo);
  }

  private String formModificationCommandsInfo() {
    return modificationCommandsPrefix
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ADDITIONAL_PATTERN, commandMediator.add, workerArgument),
            addInfo)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ARGUMENT_PATTERN, commandMediator.remove, idArgument),
            removeInfo)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(
                ADDITIONAL_PATTERN,
                String.format(ARGUMENT_PATTERN, commandMediator.update, idArgument),
                workerArgument),
            updateInfo);
  }

  private String formViewCommandsInfo() {
    return viewCommandsPrefix
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.info, infoInfo)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.show, showInfo);
  }

  private String formSpecialCommandsInfo() {
    return specialCommandsPrefix
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.help, helpInfo)
        + System.lineSeparator()
        + String.format(
            INFO_PATTERN,
            String.format(ARGUMENT_PATTERN, commandMediator.executeScript, pathArgument),
            executeScriptInfo)
        + System.lineSeparator()
        + String.format(INFO_PATTERN, commandMediator.exit, exitInfo);
  }

  @Override
  public Response executeCommand() {
    String result =
        helpPrefix
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

    logger.info(() -> "Information about commands formed.");
    return new Response(Status.OK, result);
  }
}
