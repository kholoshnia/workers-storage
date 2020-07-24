package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
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
  protected final void checkArgument(String argument, String input) throws WrongArgumentsException {
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
   */
  protected final String readArgument(String argument, Script script)
      throws WrongArgumentsException {
    Iterator<String> iterator = script.iterator();
    String input;

    if (iterator.hasNext()) {
      input = iterator.next();
    } else {
      throw new WrongArgumentsException();
    }

    String[] words = input.split(":");

    if (words.length != 2) {
      throw new WrongArgumentsException();
    }

    String name = words[0];
    String value = words[1];

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
   * @return ready arguments
   * @see #readArgument(String, Script)
   */
  protected final Map<String, String> readArguments(List<String> arguments, Script script)
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
