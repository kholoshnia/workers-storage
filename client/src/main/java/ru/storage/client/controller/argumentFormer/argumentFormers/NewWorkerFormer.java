package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class NewWorkerFormer extends WorkerFormer {
  private final Logger logger;

  private String wrongArgumentsNumberException;

  public NewWorkerFormer(
      Map<String, ArgumentValidator> argumentValidatorMap,
      ArgumentMediator argumentMediator,
      Console console) {
    super(argumentValidatorMap, argumentMediator, console);
    logger = LogManager.getLogger(NewWorkerFormer.class);
  }

  @Override
  public void changeLocale() {
    super.changeLocale();
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
    Map<String, String> allArguments = formWorker();
    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
