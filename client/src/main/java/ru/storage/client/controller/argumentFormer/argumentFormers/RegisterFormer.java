package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// TODO: Check password and login using regex.
public final class RegisterFormer extends Former implements LocaleListener {
  private final Logger logger;
  private final Console console;
  private final ArgumentMediator argumentMediator;

  private Map<String, String> registerOffers;
  private String passwordOffer;

  private String wrongArgumentsNumberException;

  public RegisterFormer(
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(console, validatorMap);
    logger = LogManager.getLogger(RegisterFormer.class);
    this.console = console;
    this.argumentMediator = argumentMediator;
  }

  private Map<String, String> initRegisterOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.USER_NAME,
            String.format("%s: ", resourceBundle.getString("offers.name")));
        put(
            argumentMediator.USER_LOGIN,
            String.format("%s: ", resourceBundle.getString("offers.login")));
      }
    };
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.RegisterFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    registerOffers = initRegisterOffers(resourceBundle);
    passwordOffer = String.format("%s: ", resourceBundle.getString("offers.password"));
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 0) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = readArguments(registerOffers, null, null);

    String input =
        readArgument(argumentMediator.USER_PASSWORD, passwordOffer, null, Character.MIN_VALUE);
    allArguments.put(argumentMediator.USER_LOGIN, input);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
