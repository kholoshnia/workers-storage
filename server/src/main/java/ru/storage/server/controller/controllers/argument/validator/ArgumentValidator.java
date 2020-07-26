package ru.storage.server.controller.controllers.argument.validator;

import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongNumberException;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongValueException;

import java.util.*;

public abstract class ArgumentValidator {
  protected static final String WRONG_ARGUMENTS_NUMBER_EXCEPTION;
  protected static final String WRONG_ARGUMENTS_VALUE_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ArgumentValidator");

    WRONG_ARGUMENTS_NUMBER_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    WRONG_ARGUMENTS_VALUE_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsValue");
  }

  protected List<String> requiredArguments;

  public ArgumentValidator() {
    requiredArguments = new ArrayList<>();
  }

  /**
   * Checks arguments number.
   *
   * @param arguments command arguments
   * @throws WrongNumberException - in case of wrong arguments number
   */
  protected void checkNumber(Map<String, String> arguments) throws WrongNumberException {
    if (arguments.size() != requiredArguments.size()) {
      throw new WrongNumberException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  /**
   * Checks argument value.
   *
   * @param arguments command arguments
   * @throws WrongValueException - in case of wrong arguments value
   */
  protected void checkValue(Map<String, String> arguments) throws WrongValueException {
    Set<String> argumentNames = arguments.keySet();

    for (String argumentName : argumentNames) {
      if (!requiredArguments.contains(argumentName)) {
        throw new WrongValueException(WRONG_ARGUMENTS_VALUE_EXCEPTION);
      }
    }
  }

  /**
   * Checks argument number using {@link #checkNumber(Map)} and value using {@link
   * #checkValue(Map)}.
   *
   * @param arguments command arguments
   * @throws WrongNumberException - in case of wrong arguments number
   * @throws WrongValueException - in case of wrong arguments value
   */
  public final void check(Map<String, String> arguments)
      throws WrongNumberException, WrongValueException {
    checkNumber(arguments);
    checkValue(arguments);
  }
}
