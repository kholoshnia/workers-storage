package ru.storage.client.controller.argumentFormer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public abstract class ArgumentFormer implements LocaleListener {
  private final Logger logger;
  private final Map<String, ArgumentValidator> argumentValidatorMap;

  @Inject
  public ArgumentFormer(Map<String, ArgumentValidator> argumentValidatorMap) {
    logger = LogManager.getLogger(ArgumentFormer.class);
    this.argumentValidatorMap = argumentValidatorMap;
  }

  /**
   * Checks input according to the specified argument.
   *
   * @param argument concrete argument
   * @param input user input
   * @throws ValidationException - if specified argument is wrong
   */
  protected final void checkArgument(String argument, String input) throws ValidationException {
    ArgumentValidator argumentValidator = argumentValidatorMap.get(argument);

    logger.info(
        "Got argument validator: {}, for argument: {}.", () -> argumentValidator, () -> argument);
    argumentValidator.check(input);
  }

  /**
   * Checks number of arguments and its values.
   *
   * @param arguments command arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  public abstract void check(List<String> arguments) throws WrongArgumentsException;

  /**
   * Forms all arguments with specified arguments.
   *
   * @param arguments command arguments
   * @return formed arguments
   * @throws WrongArgumentsException - in case of forming exception
   */
  public abstract Map<String, String> form(List<String> arguments) throws BuildingException;
}
