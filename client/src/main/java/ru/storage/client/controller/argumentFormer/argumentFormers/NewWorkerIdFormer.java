package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class NewWorkerIdFormer extends WorkerFormer {
  private final Logger logger;

  private String wrongArgumentsNumberException;

  public NewWorkerIdFormer(
      Map<String, ArgumentValidator> argumentValidatorMap,
      ArgumentMediator argumentMediator,
      Console console) {
    super(argumentValidatorMap, argumentMediator, console);
    logger = LogManager.getLogger(NewWorkerIdFormer.class);
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.NewWorkerIdFormer");

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
      throw new WrongArgumentsException(e);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.WORKER_ID, arguments.get(0));

    Map<String, String> workerArguments = formWorker();
    logger.info(() -> "Worker arguments were formed.");

    allArguments.putAll(workerArguments);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
