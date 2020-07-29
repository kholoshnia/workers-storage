package ru.storage.client.controller.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.controller.validator.validators.LoginValidator;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.*;

public final class LoginFormer extends Former implements LocaleListener {
  private static final Logger logger = LogManager.getLogger(LoginFormer.class);

  private final ArgumentMediator argumentMediator;
  private final LoginValidator loginValidator;

  private String passwordOffer;

  private String wrongArgumentsNumberException;

  @Inject
  public LoginFormer(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator,
      LoginValidator loginValidator) {
    super(commandMediator, console, validatorMap);
    this.argumentMediator = argumentMediator;
    this.loginValidator = loginValidator;
  }

  @Override
  public void changeLocale(Locale locale) {
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
      loginValidator.checkLogin(arguments.get(0));
    } catch (ValidationException e) {
      logger.warn(() -> "Got wrong arguments.", e);
      throw new WrongArgumentsException(e.getMessage());
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.userLogin, arguments.get(0));

    boolean wrong = true;

    while (wrong) {
      console.write(passwordOffer);
      logger.info("Offered user input: {}.", () -> passwordOffer);

      String input = console.readLine(null, '*');

      try {
        loginValidator.checkPassword(input);
        allArguments.put(argumentMediator.userPassword, input);
        wrong = false;
      } catch (ValidationException e) {
        console.writeLine(e.getMessage());
      }
    }

    console.setUser(allArguments.get(argumentMediator.userLogin));
    logger.info(() -> "Set login for console prompt.");

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
