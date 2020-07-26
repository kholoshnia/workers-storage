package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongNumberException;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AddValidator extends ArgumentValidator {
  @Inject
  public AddValidator(ArgumentMediator argumentMediator) {
    requiredArguments = initRequiredArguments(argumentMediator);
  }

  private List<String> initRequiredArguments(ArgumentMediator argumentMediator) {
    return new ArrayList<String>() {
      {
        add(argumentMediator.WORKER);
        add(argumentMediator.WORKER_SALARY);
        add(argumentMediator.WORKER_STATUS);
        add(argumentMediator.WORKER_START_DATE);
        add(argumentMediator.WORKER_END_DATE);
        add(argumentMediator.COORDINATES);
        add(argumentMediator.COORDINATES_X);
        add(argumentMediator.COORDINATES_Y);
        add(argumentMediator.COORDINATES_Z);
        add(argumentMediator.PERSON);
        add(argumentMediator.PERSON_NAME);
        add(argumentMediator.PERSON_PASSPORT_ID);
        add(argumentMediator.LOCATION);
        add(argumentMediator.LOCATION_ADDRESS);
        add(argumentMediator.LOCATION_LATITUDE);
        add(argumentMediator.LOCATION_LONGITUDE);
      }
    };
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
      if (!requiredArguments.contains(argumentName)) {
        throw new WrongValueException(WRONG_ARGUMENTS_VALUE_EXCEPTION);
      }
    }
  }
}
