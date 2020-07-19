package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// TODO: Do not check password using regex.
public final class LoginFormer extends Former implements LocaleListener {
  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private String passwordOffer;

  private String wrongArgumentsNumberException;

  public LoginFormer(
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(console, validatorMap);
    logger = LogManager.getLogger(LoginFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LoginFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    passwordOffer = String.format("%s: ", resourceBundle.getString("offers.password"));
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    try {
      checkArgument(argumentMediator.USER_LOGIN, arguments.get(0));
    } catch (ValidationException e) {
      logger.warn(() -> "Got wrong arguments.", e);
      throw new WrongArgumentsException(e);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = new HashMap<>();

    String input =
        readArgument(argumentMediator.USER_PASSWORD, passwordOffer, null, Character.MIN_VALUE);
    allArguments.put(argumentMediator.USER_LOGIN, input);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
