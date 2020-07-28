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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ScriptExecutor {
  private static final Logger logger = LogManager.getLogger(ScriptExecutor.class);

  private static final String ERROR_NEAR_PATTERN = "%s: %d %s - %s";

  private final Pattern regex;
  private final List<Integer> scriptList;
  private final Map<String, CommandFactory> commandFactoryMap;
  private final FormerMediator formerMediator;
  private final List<Status> stopStatuses;

  @Inject
  public ScriptExecutor(
      Map<String, CommandFactory> commandFactoryMap, FormerMediator formerMediator) {
    regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    this.commandFactoryMap = commandFactoryMap;
    this.formerMediator = formerMediator;
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

  /**
   * Executes {@link Script}.
   *
   * @param script script to execute
   * @return execution response
   */
  public Response execute(Script script) {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ScriptExecutor", script.getLocale());
    String alreadyExecutedAnswer = resourceBundle.getString("answers.alreadyExecuted");
    String noSuchCommandAnswer = resourceBundle.getString("answers.noSuchCommand");
    String errorNear = resourceBundle.getString("answers.errorNear");
    String userNotFound = resourceBundle.getString("answers.userNotFound");
    String commandCreationErrorAnswer = resourceBundle.getString("answers.commandCreationError");
    String commandNotSupportedAnswer = resourceBundle.getString("answers.commandNotSupported");

    Integer hash = script.hashCode();
    if (scriptList.contains(hash)) {
      logger.error(() -> "Script was already executed, recursion detected.");
      scriptList.remove(hash);
      return new Response(Status.BAD_REQUEST, alreadyExecutedAnswer);
    }
    scriptList.add(hash);

    StringBuilder answer =
        new StringBuilder()
            .append(resourceBundle.getString("prefixes.scriptStart"))
            .append(System.lineSeparator())
            .append(System.lineSeparator());

    Iterator<String> iterator = script.iterator();

    int counter = 0;
    while (iterator.hasNext()) {
      counter++;
      String line = iterator.next();

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
            String.format("%s: %d %s - %s", errorNear, counter, line, noSuchCommandAnswer));
      }

      Map<String, String> allArguments;

      try {
        allArguments = argumentFormer.formArguments(arguments, iterator);
      } catch (WrongArgumentsException | FormingException e) {
        scriptList.remove(hash);
        return new Response(
            Status.BAD_REQUEST,
            String.format(ERROR_NEAR_PATTERN, errorNear, counter, line, e.getMessage()));
      }

      CommandFactory commandFactory = commandFactoryMap.get(commandName);

      if (commandFactory == null) {
        logger.error(() -> "There is no such command, factory was not created.");
        scriptList.remove(hash);
        return new Response(
            Status.BAD_REQUEST,
            String.format(ERROR_NEAR_PATTERN, errorNear, counter, line, noSuchCommandAnswer));
      }

      Command command;

      try {
        command =
            commandFactory.createCommand(
                commandName, allArguments, script.getLocale(), script.getUser().getLogin());
      } catch (UserNotFoundException e) {
        logger.warn(() -> "User was not found", e);
        scriptList.remove(hash);
        return new Response(Status.NOT_FOUND, userNotFound);
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
            String.format(ERROR_NEAR_PATTERN, errorNear, counter, line, response.getAnswer()));
      }
    }

    answer.append(resourceBundle.getString("prefixes.scriptEnd"));
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
