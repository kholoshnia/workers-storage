package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class NewWorkerIdFormer implements ArgumentFormer {
  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;
  private String wrongArgumentValueException;

  public NewWorkerIdFormer(ArgumentMediator argumentMediator) {
    logger = LogManager.getLogger(NewWorkerIdFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.NewWorkerIdFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    wrongArgumentValueException = resourceBundle.getString("exceptions.wrongArgumentValue");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    String idArgument = arguments.get(0);

    long id;

    try {
      id = Long.parseLong(idArgument);
    } catch (NumberFormatException | NullPointerException exception) {
      logger.warn(() -> "Got wrong argument value.");
      throw new WrongArgumentsException(wrongArgumentValueException);
    }

    if (id < 0) {
      logger.warn(() -> "Got wrong argument value.");
      throw new WrongArgumentsException(wrongArgumentValueException);
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
