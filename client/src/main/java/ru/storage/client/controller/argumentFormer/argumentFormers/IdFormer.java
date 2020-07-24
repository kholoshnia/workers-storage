package ru.storage.client.controller.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.*;

public final class IdFormer extends Former implements LocaleListener {
  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;

  @Inject
  public IdFormer(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(commandMediator, console, validatorMap);
    logger = LogManager.getLogger(IdFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.IdFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    try {
      checkArgument(argumentMediator.WORKER_ID, arguments.get(0));
    } catch (ValidationException e) {
      logger.warn(() -> "Got wrong argument.", e);
      throw new WrongArgumentsException(e.getMessage());
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.WORKER_ID, arguments.get(0));

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
