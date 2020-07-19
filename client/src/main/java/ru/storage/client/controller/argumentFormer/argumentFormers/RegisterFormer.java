package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.*;

// TODO: Check password and login using regex.
public final class RegisterFormer extends ArgumentFormer {
  private final Logger logger;
  private final Console console;
  private final ArgumentMediator argumentMediator;

  private Map<String, String> registerOffers;

  private boolean wrong;

  private String wrongArgumentsNumberException;

  public RegisterFormer(
      Map<String, ArgumentValidator> argumentValidatorMap,
      Console console,
      ArgumentMediator argumentMediator) {
    super(argumentValidatorMap);
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
        put(
            argumentMediator.USER_PASSWORD,
            String.format("%s: ", resourceBundle.getString("offers.password")));
      }
    };
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.RegisterFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    registerOffers = initRegisterOffers(resourceBundle);
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
    Map<String, String> allArguments = new HashMap<>();

    for (Map.Entry<String, String> offerEntry : registerOffers.entrySet()) {
      String argument = offerEntry.getKey();
      String offer = offerEntry.getValue();

      wrong = true;
      while (wrong) {
        console.write(offer);
        logger.info("Offered user input: {}.", () -> offer);

        String input = console.readLine(null, null); // TODO: Ability to cancel

        try {
          checkArgument(argument, input);
          allArguments.put(argument, input);
          wrong = false;
        } catch (ValidationException e) {
          console.writeLine(e.getMessage());
        }
      }
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
