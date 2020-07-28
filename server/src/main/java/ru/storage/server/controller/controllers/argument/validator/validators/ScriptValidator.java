package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongNumberException;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongValueException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class ScriptValidator extends ArgumentValidator {
  private final Pattern scriptLinePattern;

  @Inject
  public ScriptValidator(ArgumentMediator argumentMediator) {
    super(new ArrayList<>());
    scriptLinePattern = Pattern.compile(String.format("^%s\\d+$", argumentMediator.scriptLine));
  }

  @Override
  protected void checkNumber(Map<String, String> arguments) throws WrongNumberException {
    if (arguments.size() == 0) {
      throw new WrongNumberException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  protected void checkValue(Map<String, String> arguments) throws WrongValueException {
    Set<String> argumentNames = arguments.keySet();

    for (String argumentName : argumentNames) {
      if (!scriptLinePattern.matcher(argumentName).matches()) {
        throw new WrongValueException(WRONG_ARGUMENTS_VALUE_EXCEPTION);
      }
    }
  }
}
