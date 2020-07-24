package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;
import ru.storage.server.controller.controllers.argument.validator.exceptions.WrongValueException;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class IdValidator extends ArgumentValidator {
  private final String idArgument;
  private final Parser parser;

  @Inject
  public IdValidator(ArgumentMediator argumentMediator, Parser parser) {
    requiredArguments = initRequiredArguments(argumentMediator);
    idArgument = argumentMediator.WORKER_ID;
    this.parser = parser;
  }

  private List<String> initRequiredArguments(ArgumentMediator argumentMediator) {
    return new ArrayList<String>() {
      {
        add(argumentMediator.WORKER_ID);
      }
    };
  }

  @Override
  protected void checkValue(Map<String, String> arguments) throws WrongValueException {
    super.checkValue(arguments);

    String idString = arguments.get(idArgument);

    try {
      parser.parseLong(idString);
    } catch (ParserException e) {
      throw new WrongValueException(e);
    }
  }
}
