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

public final class NewWorkerFormer implements ArgumentFormer {
  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumberException;

  public NewWorkerFormer(ArgumentMediator argumentMediator) {
    this.logger = LogManager.getLogger(NewWorkerFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.NewWorkerFormer");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
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
    logger.warn(() -> "No additional arguments required.");
    return new HashMap<>();
  }
}
