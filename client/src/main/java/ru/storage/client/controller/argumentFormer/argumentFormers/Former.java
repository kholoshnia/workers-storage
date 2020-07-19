package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;

import java.util.HashMap;
import java.util.Map;

public abstract class Former implements ArgumentFormer {
  protected final Console console;

  private final Logger logger;
  private final Map<String, ArgumentValidator> validatorMap;

  public Former(Console console, Map<String, ArgumentValidator> validatorMap) {
    logger = LogManager.getLogger(Former.class);
    this.console = console;
    this.validatorMap = validatorMap;
  }

  /**
   * Checks input according to the specified argument.
   *
   * @param argument concrete argument
   * @param input user input
   * @throws ValidationException - if specified argument is wrong
   */
  protected final void checkArgument(String argument, String input) throws ValidationException {
    ArgumentValidator argumentValidator = validatorMap.get(argument);

    logger.info(
        "Got argument validator: {}, for argument: {}.", () -> argumentValidator, () -> argument);
    argumentValidator.check(input);
  }

  /**
   * Reads an argument and checks it using validators.
   *
   * @param argument argument name
   * @param offer offer to the user
   * @param prompt user prompt
   * @param mask input mask
   * @return ready argument
   */
  protected final String readArgument(
      String argument, String offer, String prompt, Character mask) {
    while (true) {
      console.write(offer);
      logger.info("Offered user input: {}.", () -> offer);

      String input = console.readLine(prompt, mask).trim();

      try {
        checkArgument(argument, input);
        return input;
      } catch (ValidationException e) {
        console.writeLine(e.getMessage());
      }
    }
  }

  /**
   * Reads a list of arguments. Uses null prompt and mask.
   *
   * @param offers argument name, offer map
   * @return ready arguments
   * @see #readArgument(String, String, String, Character)
   */
  protected final Map<String, String> readArguments(Map<String, String> offers) {
    Map<String, String> allArguments = new HashMap<>();

    for (Map.Entry<String, String> offerEntry : offers.entrySet()) {
      String argument = offerEntry.getKey();
      String offer = offerEntry.getValue();

      String input = readArgument(argument, offer, null, null);
      allArguments.put(argument, input);
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
