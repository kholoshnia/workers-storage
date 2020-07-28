package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongNumberException;

import java.util.Map;

public final class NewWorkerValidator extends WorkerValidator {
  @Inject
  public NewWorkerValidator(ArgumentMediator argumentMediator) {
    super(argumentMediator);
  }

  @Override
  protected void checkNumber(Map<String, String> arguments) throws WrongNumberException {
    if (arguments.size() == 0) {
      throw new WrongNumberException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }
}
