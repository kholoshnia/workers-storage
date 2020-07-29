package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.CommandMediator;

import java.util.HashMap;
import java.util.Map;

public abstract class Former extends ArgumentFormer {
  private static final Logger logger = LogManager.getLogger(Former.class);

  protected final Console console;
  protected final CommandMediator commandMediator;

  private final Map<String, ArgumentValidator> validatorMap;

  public Former(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap) {
    this.console = console;
    this.commandMediator = commandMediator;
    this.validatorMap = validatorMap;
  }

  /**
   * Checks input according to the specified argument.
   *
   * @param argument command argument
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
   * Asks user whether to input next argument.
   *
   * @param question question to the user
   * @return true if user answer was "yes" else false if user answer was "no"
   * @throws CancelException - if forming was canceled
   */
  protected final boolean readArgumentQuestion(String question) throws CancelException {
    while (true) {
      console.write(String.format("%s [y/n]: ", question));
      logger.info(() -> "Asked y/n question.");

      String input = console.readLine(null, null);

      if (input == null) {
        continue;
      }

      if (input.equals(commandMediator.exit)) {
        throw new CancelException();
      }

      input = input.trim();

      if (input.equalsIgnoreCase("y")) {
        return true;
      }

      if (input.equalsIgnoreCase("n")) {
        return false;
      }
    }
  }

  /**
   * Reads an argument and checks it using validators.
   *
   * @param argument argument name
   * @param offer offer to the user
   * @param prompt user prompt
   * @param mask input mask
   * @return ready argument
   * @throws CancelException - if forming was canceled
   */
  protected final String readArgument(String argument, String offer, String prompt, Character mask)
      throws CancelException {
    while (true) {
      console.write(offer);
      logger.info("Offered user input: {}.", () -> offer);

      String input = console.readLine(prompt, mask).trim();

      if (input.equals(commandMediator.exit)) {
        throw new CancelException();
      }

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
   * @throws CancelException - if forming was canceled
   */
  protected final Map<String, String> readArguments(Map<String, String> offers)
      throws CancelException {
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
