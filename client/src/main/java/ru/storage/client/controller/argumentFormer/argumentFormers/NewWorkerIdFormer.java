package ru.storage.client.controller.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.*;

public final class NewWorkerIdFormer extends WorkerFormer {
  private static final Logger logger = LogManager.getLogger(NewWorkerIdFormer.class);

  private String wrongArgumentsNumberException;

  @Inject
  public NewWorkerIdFormer(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(commandMediator, console, validatorMap, argumentMediator);
  }

  @Override
  public void changeLocale(Locale locale) {
    super.changeLocale(locale);
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
      checkArgument(argumentMediator.workerId, arguments.get(0));
    } catch (ValidationException e) {
      logger.warn(() -> "Got wrong argument.", e);
      throw new WrongArgumentsException(e.getMessage());
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments) throws CancelException {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.workerId, arguments.get(0));

    Map<String, String> workerArguments = formWorker();
    logger.info(() -> "Worker arguments were formed.");

    allArguments.putAll(workerArguments);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
