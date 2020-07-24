package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class NewWorkerFormer extends WorkerFormer {
  private static final String WRONG_ARGUMENTS_NUMBER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.NewWorkerFormer");

    WRONG_ARGUMENTS_NUMBER_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  private final Logger logger;

  @Inject
  public NewWorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    logger = LogManager.getLogger(NewWorkerFormer.class);
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 0) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Iterator<String> script)
      throws FormingException {
    Map<String, String> allArguments;

    try {
      allArguments = formWorker(script);
    } catch (WrongArgumentsException e) {
      logger.info(() -> "Cannot form worker from script.", e);
      throw new FormingException(e.getMessage());
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
