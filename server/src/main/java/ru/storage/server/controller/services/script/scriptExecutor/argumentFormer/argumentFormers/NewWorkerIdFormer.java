package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public final class NewWorkerIdFormer extends WorkerFormer {
  private static final Logger logger = LogManager.getLogger(NewWorkerIdFormer.class);

  private static final String WRONG_ARGUMENTS_NUMBER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.NewWorkerIdFormer");

    WRONG_ARGUMENTS_NUMBER_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Inject
  public NewWorkerIdFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Iterator<String> script)
      throws FormingException {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.workerId, arguments.get(0));

    Map<String, String> workerArguments;

    try {
      workerArguments = formWorker(script);
    } catch (WrongArgumentsException e) {
      logger.info(() -> "Cannot form worker", e);
      throw new FormingException(e.getMessage());
    }

    logger.info(() -> "Worker arguments were formed.");

    allArguments.putAll(workerArguments);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
