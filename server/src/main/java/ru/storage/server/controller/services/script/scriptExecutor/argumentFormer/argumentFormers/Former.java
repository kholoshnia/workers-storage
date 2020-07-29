package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public abstract class Former extends ArgumentFormer {
  private static final Logger logger = LogManager.getLogger(Former.class);

  protected final ArgumentMediator argumentMediator;

  private String wrongSeparatorsNumberException;
  private String wrongArgumentException;

  public Former(ArgumentMediator argumentMediator) {
    this.argumentMediator = argumentMediator;
  }

  @Override
  protected void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.Former", locale);

    wrongSeparatorsNumberException = resourceBundle.getString("exceptions.wrongSeparatorsNumber");
    wrongArgumentException = resourceBundle.getString("exceptions.wrongArgument");
  }

  /**
   * Reads a list of arguments. Uses null prompt and mask.
   *
   * @param arguments required arguments
   * @param script script to read arguments from
   * @return ready arguments
   * @throws WrongArgumentsException - if specified argument is wrong
   */
  protected final Map<String, String> readArguments(List<String> arguments, Script script)
      throws WrongArgumentsException {
    Map<String, String> allArguments = new HashMap<>();

    while (script.hasNext()) {
      String input = script.nextLine();

      if (input == null || input.isEmpty()) {
        continue;
      }

      if (!input.contains(":")) {
        script.back();
        break;
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
        throw new WrongArgumentsException(wrongSeparatorsNumberException);
      }

      if (!arguments.contains(name)) {
        throw new WrongArgumentsException(wrongArgumentException);
      }

      allArguments.put(name, value);
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
