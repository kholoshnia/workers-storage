package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// TODO: Do not check password using regex.
public final class LoginFormer extends ArgumentFormer {
  private final Logger logger;
  private final Console console;
  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;

  public LoginFormer(
      Map<String, ArgumentValidator> argumentValidatorMap,
      ArgumentMediator argumentMediator,
      Console console) {
    super(argumentValidatorMap);
    logger = LogManager.getLogger(LoginFormer.class);
    this.argumentMediator = argumentMediator;
    this.console = console;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LoginFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 2) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    try {
      checkArgument(argumentMediator.USER_LOGIN, arguments.get(0));
      checkArgument(argumentMediator.USER_PASSWORD, arguments.get(1));
    } catch (ValidationException e) {
      logger.warn(() -> "Got wrong arguments.", e);
      throw new WrongArgumentsException(e);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = new HashMap<>();

    allArguments.put(argumentMediator.USER_LOGIN, arguments.get(0));
    allArguments.put(argumentMediator.USER_PASSWORD, arguments.get(1));

    console.setLogin(allArguments.get(argumentMediator.USER_LOGIN));
    logger.info(() -> "Console user login was set.");

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
