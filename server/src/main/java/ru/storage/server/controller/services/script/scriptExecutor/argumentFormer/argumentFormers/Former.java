package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Former extends ArgumentFormer {
  protected final ArgumentMediator argumentMediator;

  private final Logger logger;

  public Former(ArgumentMediator argumentMediator) {
    logger = LogManager.getLogger(Former.class);
    this.argumentMediator = argumentMediator;
  }

  /**
   * Checks input according to the specified argument.
   *
   * @param argument concrete argument
   * @param input user input
   * @throws WrongArgumentsException - if specified argument is wrong
   */
  protected void checkArgument(String argument, String input) throws WrongArgumentsException {
    if (argumentMediator.contains(argument) && input != null && !input.isEmpty()) {
      return;
    }

    throw new WrongArgumentsException();
  }

  /**
   * Reads an argument and checks it using validators.
   *
   * @param argument argument name
   * @param script script to read argument from
   * @return ready argument
   * @throws WrongArgumentsException - if specified argument is wrong
   */
  protected final String readArgument(String argument, Iterator<String> script)
      throws WrongArgumentsException {
    String input;

    if (script.hasNext()) {
      input = script.next();
    } else {
      throw new WrongArgumentsException();
    }

    String[] words = input.split(":", 2);

    String name;
    String value;

    if (words.length == 2) {
      name = words[0].trim();
      value = words[1].trim();
    } else if (words.length == 1) {
      name = words[0].trim();
      value = null;
    } else {
      throw new WrongArgumentsException();
    }

    if (!argument.equals(name)) {
      throw new WrongArgumentsException();
    }

    checkArgument(name, value);

    logger.info(() -> "Got value from script.");
    return value;
  }

  /**
   * Reads a list of arguments. Uses null prompt and mask.
   *
   * @param arguments argument names
   * @param script script to read arguments from
   * @return ready arguments
   * @see #readArgument(String, Iterator)
   * @throws WrongArgumentsException - if specified argument is wrong
   */
  protected final Map<String, String> readArguments(List<String> arguments, Iterator<String> script)
      throws WrongArgumentsException {
    Map<String, String> allArguments = new HashMap<>();

    for (String argument : arguments) {
      String input = readArgument(argument, script);
      allArguments.put(argument, input);
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
