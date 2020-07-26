package ru.storage.client.controller.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.*;

public final class RegisterFormer extends Former implements LocaleListener {
  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private Map<String, String> registerOffers;
  private String passwordOffer;

  private String wrongArgumentsNumberException;

  @Inject
  public RegisterFormer(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(commandMediator, console, validatorMap);
    logger = LogManager.getLogger(RegisterFormer.class);
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
  public void changeLocale(Locale locale) {
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
  public Map<String, String> form(List<String> arguments) throws CancelException {
    Map<String, String> allArguments = readArguments(registerOffers);

    String input = readArgument(argumentMediator.USER_PASSWORD, passwordOffer, null, '*');
    allArguments.put(argumentMediator.USER_PASSWORD, input);

    console.setUser(allArguments.get(argumentMediator.USER_LOGIN));
    logger.info(() -> "Set login for console prompt.");

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
