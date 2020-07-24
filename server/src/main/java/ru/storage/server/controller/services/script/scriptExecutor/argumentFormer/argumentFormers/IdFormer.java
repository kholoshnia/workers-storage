package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public final class IdFormer extends Former {
  private static final String WRONG_ARGUMENTS_NUMBER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.IdFormer");

    WRONG_ARGUMENTS_NUMBER_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  private final Logger logger;
  private final ArgumentMediator argumentMediator;

  @Inject
  public IdFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    logger = LogManager.getLogger(IdFormer.class);
    this.argumentMediator = argumentMediator;
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Iterator<String> script) {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.WORKER_ID, arguments.get(0));

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
