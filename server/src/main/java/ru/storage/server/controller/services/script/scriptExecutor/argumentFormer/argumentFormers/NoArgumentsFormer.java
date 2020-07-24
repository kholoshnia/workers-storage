package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public final class NoArgumentsFormer extends ArgumentFormer {
  private static final String WRONG_ARGUMENTS_NUMBER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.NoArgumentsFormer");

    WRONG_ARGUMENTS_NUMBER_EXCEPTION = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  private final Logger logger;

  @Inject
  public NoArgumentsFormer() {
    logger = LogManager.getLogger(NoArgumentsFormer.class);
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 0) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(WRONG_ARGUMENTS_NUMBER_EXCEPTION);
    }
  }

  @Override
  protected Map<String, String> form(List<String> arguments, Iterator<String> script) {
    return new HashMap<>();
  }
}
