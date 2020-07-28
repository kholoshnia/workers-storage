package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongNumberException;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongValueException;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;

import java.util.Map;

public final class NewWorkerIdValidator extends WorkerValidator {
  private final ArgumentMediator argumentMediator;
  private final Parser parser;

  @Inject
  public NewWorkerIdValidator(ArgumentMediator argumentMediator, Parser parser) {
    super(argumentMediator);
    requiredArguments.add(argumentMediator.workerId);
    this.argumentMediator = argumentMediator;
    this.parser = parser;
  }

  @Override
  protected void checkNumber(Map<String, String> arguments) throws WrongNumberException {
    if (arguments.size() <= 1) {
      throw new WrongNumberException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  protected void checkValue(Map<String, String> arguments) throws WrongValueException {
    super.checkValue(arguments);

    String idString = arguments.get(argumentMediator.workerId);

    try {
      parser.parseLong(idString);
    } catch (ParserException e) {
      throw new WrongValueException(e);
    }
  }
}
