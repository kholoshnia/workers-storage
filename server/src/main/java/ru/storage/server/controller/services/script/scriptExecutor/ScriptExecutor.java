package ru.storage.server.controller.services.script.scriptExecutor;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.controllers.command.factory.exceptions.UserNotFoundException;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.FormerMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.server.model.domain.history.History;
import ru.storage.server.model.domain.history.Record;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ScriptExecutor {
  private static final Logger logger = LogManager.getLogger(ScriptExecutor.class);

  private static final String ERROR_NEAR_PATTERN = "%s: %d \"%s\" - %s";

  private final Pattern regex;
  private final List<Integer> scriptList;
  private final Map<String, CommandFactory> commandFactoryMap;
  private final FormerMediator formerMediator;
  private final History history;
  private final List<Status> stopStatuses;

  private String scriptStartPrefix;
  private String errorArLinePrefix;
  private String scriptEndPrefix;

  private String alreadyExecutedAnswer;
  private String noSuchCommandAnswer;
  private String userNotFoundAnswer;
  private String commandCreationErrorAnswer;
  private String commandNotSupportedAnswer;

  @Inject
  public ScriptExecutor(
      Map<String, CommandFactory> commandFactoryMap,
      FormerMediator formerMediator,
      History history) {
    regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    this.commandFactoryMap = commandFactoryMap;
    this.formerMediator = formerMediator;
    this.history = history;
    stopStatuses = initStopStatuses();
    scriptList = new ArrayList<>();
  }

  private List<Status> initStopStatuses() {
    return new ArrayList<Status>() {
      {
        add(Status.BAD_REQUEST);
        add(Status.INTERNAL_SERVER_ERROR);
      }
    };
  }

  private void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ScriptExecutor", locale);

    scriptStartPrefix = resourceBundle.getString("prefixes.scriptStart");
    errorArLinePrefix = resourceBundle.getString("prefixes.errorAtLine");
    scriptEndPrefix = resourceBundle.getString("prefixes.scriptEnd");

    alreadyExecutedAnswer = resourceBundle.getString("answers.alreadyExecuted");
    noSuchCommandAnswer = resourceBundle.getString("answers.noSuchCommand");
    userNotFoundAnswer = resourceBundle.getString("answers.userNotFound");
    commandCreationErrorAnswer = resourceBundle.getString("answers.commandCreationError");
    commandNotSupportedAnswer = resourceBundle.getString("answers.commandNotSupported");
  }

  /**
   * Executes {@link Script}.
   *
   * @param script script to execute
   * @return execution response
   */
  public Response execute(Script script) {
    changeLocale(script.getLocale());

    Integer hash = script.hashCode();
    if (scriptList.contains(hash)) {
      logger.error(() -> "Script was already executed, recursion detected.");
      scriptList.remove(hash);
      return new Response(Status.BAD_REQUEST, alreadyExecutedAnswer);
    }
    scriptList.add(hash);

    StringBuilder answer =
        new StringBuilder()
            .append(scriptStartPrefix)
            .append(System.lineSeparator())
            .append(System.lineSeparator());

    while (script.hasNext()) {
      String line = script.nextLine();

      List<String> words = parse(line);

      if (words.isEmpty()) {
        continue;
      }

      String commandName = words.get(0);
      List<String> arguments;

      if (words.size() > 1) {
        arguments = words.subList(1, words.size());
      } else {
        arguments = new ArrayList<>();
      }

      ArgumentFormer argumentFormer = formerMediator.getArgumentFormer(commandName);

      if (argumentFormer == null) {
        logger.error(() -> "There is no such command, factory was not created.");
        scriptList.remove(hash);
        return new Response(
            Status.BAD_REQUEST,
            String.format(
                ERROR_NEAR_PATTERN,
                errorArLinePrefix,
                script.getCurrent(),
                line,
                noSuchCommandAnswer));
      }

      Map<String, String> allArguments;

      try {
        allArguments = argumentFormer.formArguments(arguments, script);
      } catch (WrongArgumentsException | FormingException e) {
        logger.error(() -> "Wrong command arguments.", e);
        scriptList.remove(hash);
        return new Response(
            Status.BAD_REQUEST,
            String.format(
                ERROR_NEAR_PATTERN, errorArLinePrefix, script.getCurrent(), line, e.getMessage()));
      }

      CommandFactory commandFactory = commandFactoryMap.get(commandName);

      if (commandFactory == null) {
        logger.error(() -> "There is no such command, factory was not created.");
        scriptList.remove(hash);
        return new Response(
            Status.BAD_REQUEST,
            String.format(
                ERROR_NEAR_PATTERN,
                errorArLinePrefix,
                script.getCurrent(),
                line,
                noSuchCommandAnswer));
      }

      Command command;

      try {
        command =
            commandFactory.createCommand(
                commandName, allArguments, script.getLocale(), script.getUser().getLogin());
      } catch (UserNotFoundException e) {
        logger.warn(() -> "User was not found", e);
        scriptList.remove(hash);
        return new Response(Status.NOT_FOUND, userNotFoundAnswer);
      } catch (CommandFactoryException e) {
        logger.error("Cannot create command: {}.", (Supplier<?>) () -> commandName, e);
        scriptList.remove(hash);
        return new Response(Status.INTERNAL_SERVER_ERROR, commandCreationErrorAnswer);
      }

      if (command == null) {
        logger.error(() -> "Got null command factory.");
        scriptList.remove(hash);
        return new Response(Status.INTERNAL_SERVER_ERROR, commandNotSupportedAnswer);
      }

      Response response = command.executeCommand();

      history.addRecord(new Record(commandName, allArguments, response));

      if (!stopStatuses.contains(response.getStatus())) {
        answer
            .append(String.format("%s %s:", commandName, response.getStatus()))
            .append(System.lineSeparator())
            .append(response.getAnswer())
            .append(System.lineSeparator())
            .append(System.lineSeparator());
      } else {
        scriptList.remove(hash);
        return new Response(
            response.getStatus(),
            String.format(
                ERROR_NEAR_PATTERN,
                errorArLinePrefix,
                script.getCurrent(),
                line,
                response.getAnswer()));
      }
    }

    answer.append(scriptEndPrefix);
    scriptList.remove(hash);
    logger.info(() -> "Script was executed.");
    return new Response(Status.OK, answer.toString());
  }

  /**
   * Parses string by words in a list of string. Words can be separated by spaces or can be
   * surrounded by " and ' symbols. NOTE: returns empty list if there is no words found.
   *
   * @param string string to parse
   * @return list of words from string
   */
  private List<String> parse(String string) {
    if (string == null) {
      return new ArrayList<>();
    }

    List<String> words = new ArrayList<>();
    Matcher matcher = regex.matcher(string);

    while (matcher.find()) {
      if (matcher.group(1) != null) {
        words.add(matcher.group(1));
      } else if (matcher.group(2) != null) {
        words.add(matcher.group(2));
      } else {
        words.add(matcher.group());
      }
    }

    words.replaceAll(String::trim);
    return words;
  }
}
